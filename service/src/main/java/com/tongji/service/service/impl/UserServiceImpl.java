package com.tongji.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tongji.model.dtos.LoginDTO;
import com.tongji.model.dtos.ResponseResult;
import com.tongji.model.enums.AppHttpCodeEnum;
import com.tongji.service.mapper.UserMapper;
import com.tongji.service.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.tongji.model.pojos.User;

/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-10-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

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
        if (!user.getPassword().equals(password)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        StpUtil.login(user.getId());
        return ResponseResult.okResult("登录成功");
    }
}
