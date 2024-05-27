package com.tongji.user.service.Impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tongji.common.constants.CommonConstants;
import com.tongji.common.enums.AppHttpCodeEnum;
import com.tongji.common.service.FileStorageService;
import com.tongji.common.service.Impl.CacheService;
import com.tongji.common.utils.SmsUtil;
import com.tongji.global.helper.LoginObj;
import com.tongji.global.util.SaTokenUtil;
import com.tongji.model.dto.common.DoctorDetailDTO;
import com.tongji.model.dto.patient.UserLoginDTO;
import com.tongji.model.dto.patient.UserDTO;
import com.tongji.model.dto.patient.UserDetailDTO;
import com.tongji.global.enums.RoleEnum;
import com.tongji.model.pojo.Doctor;
import com.tongji.model.pojo.DoctorDetail;
import com.tongji.model.pojo.User;
import com.tongji.model.pojo.UserDetail;
import com.tongji.model.vo.ResponseResult;
import com.tongji.user.mapper.DoctorDetailMapper;
import com.tongji.user.mapper.DoctorMapper;
import com.tongji.user.mapper.UserDetailMapper;
import com.tongji.user.mapper.UserMapper;
import com.tongji.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-10-18
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private UserDetailMapper userDetailMapper;

    @Autowired
    private Environment environment;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private DoctorDetailMapper doctorDetailMapper;


    @Override
    public ResponseResult login(UserLoginDTO userLoginDTO) {
        if (StrUtil.hasBlank(userLoginDTO.getAccount(), userLoginDTO.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "用户名或密码为空");
        }

        User user = this.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getAccount, userLoginDTO.getAccount())
        );
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户不存在");
        }

        // 对比密码
        String salt = user.getSalt();
        String password = userLoginDTO.getPassword();
        password = SaSecureUtil.md5(password + salt);
        if (!user.getPassword().equals(password)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        LoginObj loginObj = new LoginObj(RoleEnum.PATIENT.getName(), user.getId());
        JSONObject jsonUser = (JSONObject) JSONObject.toJSON(loginObj);
        //会话登录：参数填写要登录的账号id，建议的数据类型：long | int | String，
        //不可以传入复杂类型，如：User、Admin 等等
        //因此转为JSON字符串传入，需要时再转回Object
        StpUtil.login(jsonUser);

        log.info("登录成功 {}", user.getId());
        return ResponseResult.okResult("登录成功");
    }

    @Override
    public ResponseResult logout() {
        StpUtil.logout();
        return ResponseResult.okResult("登出成功");

    }

    @Override
    public ResponseResult getUser() {
        Long id = SaTokenUtil.getId();
        User user = this.getById(id);
        return ResponseResult.okResult(user);
    }

    @Override
    public ResponseResult register(UserDTO dto) {
        String phone = dto.getAccount();
        String code = dto.getCode();
        // dto 参数检查
        if (StrUtil.hasBlank(phone, dto.getPassword(), dto.getName(), code)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "属性不能为空");
        }
        // 检查手机号格式
        if (!Validator.isMobile(phone)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "手机号格式不正确");
        }
        // 检查密码只能含有数字和字母
        if (!Validator.isGeneral(dto.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "密码只能含有英文字母 、数字和下划线");
        }
        // 检查验证码是否正确
        String codeCache = this.cacheService.get(CommonConstants.SMS_CODE + phone);
        if (!code.equals(codeCache)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "验证码错误");
        }

        // 删掉验证码
        this.cacheService.delete(CommonConstants.SMS_CODE + phone);


        // 随机生成长度为6的字符串作为盐
        String salt = RandomUtil.randomString(CommonConstants.SALT_LENGTH);
        String password = SaSecureUtil.md5(dto.getPassword() + salt);

        User user = new User();
        user.setName(dto.getName());
        user.setAccount(dto.getAccount());
        user.setSalt(salt);
        user.setPassword(password);
        try {
            this.save(user);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "用户名已存在");
        }

        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(user.getId());
        userDetailService.save(userDetail);

        return ResponseResult.okResult("注册成功");
    }

    @Override
    public ResponseResult sendCode(String mobile) {
        // 手机号不能为空
        if (StrUtil.isBlank(mobile)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "手机号不能为空");
        }
        // 检查手机号格式
        if (!Validator.isMobile(mobile)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "手机号格式不正确");
        }
        // 先去查这个手机号有没有注册过
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, mobile));
        if (user != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "手机号已注册");
        }
        // 看看之前有没有发送过验证码
        String codeCache = this.cacheService.get(CommonConstants.SMS_CODE + mobile);
        if (StrUtil.isNotBlank(codeCache)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "验证码已发送");
        }
        // 生成验证码
        String code = RandomUtil.randomNumbers(CommonConstants.SMS_CODE_LENGTH);
        // 缓存验证码
        this.cacheService.setEx(CommonConstants.SMS_CODE + mobile, code, CommonConstants.SMS_CODE_TIMEOUT,
                CommonConstants.SMS_CODE_TIMEOUT_TYPE);
        // 发送验证码
        this.smsUtil.sendSms(mobile, code);
        return ResponseResult.okResult("发送成功");
    }

    @Override
    public ResponseResult sendCodeUpdate(String mobile) {
        // 手机号不能为空
        if (StrUtil.isBlank(mobile)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "手机号不能为空");
        }
        // 检查手机号格式
        if (!Validator.isMobile(mobile)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "手机号格式不正确");
        }
        // 先去查这个手机号有没有注册过
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, mobile));
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "手机号未注册");
        }
        // 看看之前有没有发送过验证码
        String codeCache = this.cacheService.get(CommonConstants.SMS_UPDATE_CODE + mobile);
        if (StrUtil.isNotBlank(codeCache)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "不可重复发送验证码");
        }
        // 生成验证码
        String code = RandomUtil.randomNumbers(CommonConstants.SMS_CODE_LENGTH);
        // 缓存验证码
        this.cacheService.setEx(CommonConstants.SMS_UPDATE_CODE + mobile, code, CommonConstants.SMS_CODE_TIMEOUT,
                CommonConstants.SMS_CODE_TIMEOUT_TYPE);
        // 发送验证码
        this.smsUtil.sendSms(mobile, code);
        return ResponseResult.okResult("发送成功");
    }

    @Override
    public ResponseResult updateName(UserDTO dto) {
        if (StrUtil.hasBlank(dto.getName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "用户名不能为空");
        }
        Long id = SaTokenUtil.getId();
        User user = this.getById(id);
        user.setName(dto.getName());
        this.updateById(user);
        return ResponseResult.okResult("修改成功");
    }

    @Override
    public ResponseResult updatePassword(UserDTO dto) {
        if (StrUtil.hasBlank(dto.getPassword()) || StrUtil.hasBlank(dto.getRePassword())
                || StrUtil.hasBlank(dto.getCode())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 检查密码只能含有数字和字母
        if (!Validator.isGeneral(dto.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "密码只能含有英文字母 、数字和下划线");
        }
        // 检查两次密码是否一致
        if (!dto.getPassword().equals(dto.getRePassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "两次密码不一致");
        }
        // 找到是哪个账号
        User user = this.getById(SaTokenUtil.getId());
        // 检查验证码是否正确
        String codeCache = this.cacheService.get(CommonConstants.SMS_UPDATE_CODE + user.getAccount());
        if (!dto.getCode().equals(codeCache)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "验证码错误");
        }
        // 删掉验证码
        this.cacheService.delete(CommonConstants.SMS_UPDATE_CODE + user.getAccount());
        String salt = user.getSalt();
        String password = SaSecureUtil.md5(dto.getPassword() + salt);

        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getId, user.getId()).set(User::getPassword, password);
        this.baseMapper.update(null, lambdaUpdateWrapper);
        // 将当前账号下线

        StpUtil.logout();

        return ResponseResult.okResult("修改成功");
    }

    @Override
    public ResponseResult forgetPassword(UserDTO dto) {
        if (StrUtil.hasBlank(dto.getAccount()) || StrUtil.hasBlank(dto.getPassword()) || StrUtil.hasBlank(dto.getRePassword())
                || StrUtil.hasBlank(dto.getCode())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 检查手机号格式
        if (!Validator.isMobile(dto.getAccount())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "手机号格式不正确");
        }
        // 比较两次密码是否一致
        if (!dto.getPassword().equals(dto.getRePassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "两次密码不一致");
        }
        // 找到是哪个账号
        User user = this.getOne(Wrappers.<User>lambdaQuery()
                        .eq(User::getAccount, dto.getAccount())
//                .eq(User::getRole, RoleEnum.valueOf(dto.getRole()).getRoleNum())
        );
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "手机号未注册");
        }
        // 检查验证码是否正确
        String codeCache = this.cacheService.get(CommonConstants.SMS_UPDATE_CODE + dto.getAccount());
        if (!dto.getCode().equals(codeCache)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "验证码错误");
        }
        // 删掉验证码
        this.cacheService.delete(CommonConstants.SMS_UPDATE_CODE + dto.getAccount());
        String salt = user.getSalt();
        String password = SaSecureUtil.md5(dto.getPassword() + salt);

        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getId, user.getId()).set(User::getPassword, password);
        this.baseMapper.update(null, lambdaUpdateWrapper);

        return ResponseResult.okResult("修改成功");
    }

    @Override
    public ResponseResult getDetail() {
        User user = this.getById(SaTokenUtil.getId());
        UserDetail userDetail = userDetailService.getOne(
                Wrappers.<UserDetail>lambdaQuery().eq(UserDetail::getUserId, user.getId())
        );
        UserDetailDTO userDetailDTO = new UserDetailDTO();

        BeanUtils.copyProperties(user, userDetailDTO);
        BeanUtils.copyProperties(userDetail, userDetailDTO);

        return ResponseResult.okResult(userDetailDTO);
    }

    @Override
    public ResponseResult updateDetail(UserDetailDTO userDetailDTO) {
        Long id = SaTokenUtil.getId();

        // 找到这条信息
        UserDetail userDetailFound = userDetailMapper.selectOne(Wrappers.<UserDetail>lambdaQuery().eq(UserDetail::getUserId,
                id));
        if (userDetailFound == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户信息不存在");
        }
        UserDetail userDetail = new UserDetail();
        userDetail.setId(userDetailFound.getId());
        userDetail.setUserId(id);
        BeanUtils.copyProperties(userDetailDTO, userDetail);
        if (userDetail.getAge() == null) {
            userDetail.setAge(0);
        }
        if (userDetail.getHeight() == null) {
            userDetail.setHeight(0.0);
        }
        if (userDetail.getWeight() == null) {
            userDetail.setWeight(0.0);
        }
        if (userDetail.getGender() == null) {
            userDetail.setGender(null);
        }
        if (userDetail.getDiabetesType() == null) {
            userDetail.setDiabetesType(null);
        }
        log.info("修改用户信息 {}", userDetail);
        userDetailMapper.updateById(userDetail);
        return ResponseResult.okResult("修改成功");
    }

    @Override
    public ResponseResult uploadProfix(MultipartFile picture) {
        if (picture.isEmpty()) {
            return ResponseResult.errorResult(400, "上传文件不能为空");
        }
        String filename = picture.getOriginalFilename();
        String fileType = filename.substring(filename.lastIndexOf("."));
        System.out.println(fileType);
        boolean isImage = fileType.equalsIgnoreCase(".jpg")
                || fileType.equalsIgnoreCase(".jpeg")
                || fileType.equalsIgnoreCase(".png");
        if (!isImage) {
            return ResponseResult.errorResult(400, "文件类型错误");
        }

        User user = this.getById(SaTokenUtil.getId());


        String prifileUrl = user.getProfile();
        if (!StrUtil.hasBlank(prifileUrl))
            fileStorageService.delete(prifileUrl);

        //将文件上传到minio
        String path = fileStorageService.uploadFile(picture);
        user.setProfile(path);

        this.updateById(user);
        Map<String, String> map = new HashMap<>();
        map.put("url", path);
        return ResponseResult.okResult(map);
    }

    public ResponseResult getDoctor() {
        Long patientId = SaTokenUtil.getId();
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, patientId));
        Long doctorId = user.getDoctor();
        if (doctorId == null) {
            return ResponseResult.errorResult(400, "未绑定医生");
        }
        Doctor doctor = doctorMapper.selectOne(
                Wrappers.<Doctor>lambdaQuery()
                        .eq(Doctor::getId, doctorId)
        );
        DoctorDetail doctorDetail = doctorDetailMapper.selectOne(
                Wrappers.<DoctorDetail>lambdaQuery()
                        .eq(DoctorDetail::getDoctorId, doctor.getId())
        );
        DoctorDetailDTO doctorDetailDTO=new DoctorDetailDTO();
        doctorDetailDTO.setAccount(doctor.getAccount());
        doctorDetailDTO.setAge(doctorDetail.getAge());
        doctorDetailDTO.setGender(doctorDetail.getGender());
        doctorDetailDTO.setName(doctor.getName());
        doctorDetailDTO.setDepartment(doctorDetail.getDepartment());
        doctorDetailDTO.setIntroduction(doctorDetail.getIntroduction());
        doctorDetailDTO.setTitle(doctorDetail.getTitle());

        return ResponseResult.okResult(doctorDetailDTO);

    }
}
