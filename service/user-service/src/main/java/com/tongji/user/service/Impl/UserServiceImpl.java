package com.tongji.user.service.Impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tongji.common.constants.CommonConstants;
import com.tongji.common.enums.AppHttpCodeEnum;
import com.tongji.common.service.Impl.CacheService;
import com.tongji.common.utils.SmsUtil;
import com.tongji.model.dto.LoginDTO;
import com.tongji.model.dto.UserDTO;
import com.tongji.model.pojo.User;
import com.tongji.model.vo.ResponseResult;
import com.tongji.user.mapper.UserMapper;
import com.tongji.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    @Override
    public ResponseResult login(LoginDTO loginDTO) {
        if (StrUtil.hasBlank(loginDTO.getAccount(), loginDTO.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "用户名或密码为空");
        }

        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, loginDTO.getAccount()));
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
        log.info("登录成功 {}", user.getId());
        StpUtil.login(user.getId());
        return ResponseResult.okResult("登录成功");
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
}
