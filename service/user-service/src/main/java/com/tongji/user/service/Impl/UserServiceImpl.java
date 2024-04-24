package com.tongji.user.service.Impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Enums;
import com.tongji.common.constants.CommonConstants;
import com.tongji.common.enums.AppHttpCodeEnum;
import com.tongji.common.service.Impl.CacheService;
import com.tongji.common.utils.SmsUtil;
import com.tongji.model.dto.LoginDTO;
import com.tongji.model.dto.UserDTO;
import com.tongji.model.dto.UserDetailDTO;
import com.tongji.global.enums.RoleEnum;
import com.tongji.global.constrants.Constrants;
import com.tongji.model.pojo.User;
import com.tongji.model.pojo.UserDetail;
import com.tongji.model.vo.ResponseResult;
import com.tongji.user.mapper.UserDetailMapper;
import com.tongji.user.mapper.UserMapper;
import com.tongji.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


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

    @Override
    public ResponseResult login(LoginDTO loginDTO) {
        if (StrUtil.hasBlank(loginDTO.getAccount(), loginDTO.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "用户名或密码为空");
        }

        User user = this.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getAccount, loginDTO.getAccount())
                .eq(User::getRole, RoleEnum.valueOf(loginDTO.getRole()).getRoleNum())
        );
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户不存在");
        }

        // 对比密码
        String salt = user.getSalt();
        String password = loginDTO.getPassword();
        password = SaSecureUtil.md5(password + salt);
        if (!user.getPassword().equals(password)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        StpUtil.login(user.getId());

        //将角色写入redis
        cacheService.delete(Constrants.USER_ROLE + user.getId());
        cacheService.set(Constrants.USER_ROLE + user.getId(), loginDTO.getRole());

        String expireString = environment.getProperty("sa-token.timeout");
        if (StrUtil.hasBlank(expireString))
            expireString = "14400";
        long expire = Long.parseLong(expireString);
        cacheService.expire(Constrants.USER_ROLE + user.getId(), expire, TimeUnit.SECONDS);
        log.info("登录成功 {}", user.getId());
        return ResponseResult.okResult("登录成功");
    }

    @Override
    public ResponseResult logout() {
        User user = this.getById(StpUtil.getLoginIdAsLong());
        //删除角色缓存
        cacheService.delete(Constrants.USER_ROLE + user.getId());
        StpUtil.logout();
        return ResponseResult.okResult("登出成功");

    }

    @Override
    public ResponseResult register(UserDTO dto) {
        String phone = dto.getAccount();
        String code = dto.getCode();
        // dto 参数检查
        if (StrUtil.hasBlank(phone, dto.getPassword(), dto.getName(), code, dto.getRole())) {
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

        RoleEnum roleEnum = Enums.getIfPresent(RoleEnum.class, dto.getRole()).orNull();
        if (roleEnum == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "角色不存在");
        }
        // 删掉验证码
        this.cacheService.delete(CommonConstants.SMS_CODE + phone);

//        User user = this.getOne(Wrappers.<User>lambdaQuery()
//                .eq(User::getAccount, dto.getAccount())
//                .eq(User::getRole,dto.getRole())
//        );
//        if(user!=null){
//            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "用户已存在");
//        }

        // 随机生成长度为6的字符串作为盐
        String salt = RandomUtil.randomString(CommonConstants.SALT_LENGTH);
        String password = SaSecureUtil.md5(dto.getPassword() + salt);

        User user = new User();
        user.setName(dto.getName());
        user.setAccount(dto.getAccount());
        user.setSalt(salt);
        user.setPassword(password);
        user.setRole(RoleEnum.valueOf(dto.getRole()).getRoleNum());
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
//        // 先去查这个手机号有没有注册过
//        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, mobile));
//        if (user != null) {
//            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "手机号已注册");
//        }
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
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "验证码已发送");
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
        User user = this.getById(StpUtil.getLoginIdAsLong());
        log.info("修改用户名 {}", StpUtil.getLoginIdAsLong());
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
        User user = this.getById(StpUtil.getLoginIdAsLong());
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

        //删掉角色信息
        cacheService.delete(Constrants.USER_ROLE + user.getId());

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
                .eq(User::getRole, RoleEnum.valueOf(dto.getRole()).getRoleNum())
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
        User user = this.getById(StpUtil.getLoginIdAsLong());
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
        Long id = StpUtil.getLoginIdAsLong();
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
}
