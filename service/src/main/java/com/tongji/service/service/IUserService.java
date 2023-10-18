package com.tongji.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.dtos.LoginDTO;
import com.tongji.model.vos.ResponseResult;
import com.tongji.model.dtos.UserDTO;
import com.tongji.model.pojos.User;

/**
 * <p>
 * 员工信息 服务类
 * </p>
 *
 * @author author
 * @since 2023-10-18
 */
public interface IUserService extends IService<User> {
    ResponseResult login(LoginDTO loginDTO);

    ResponseResult register(UserDTO dto);

    ResponseResult sendCode(String mobile);
}
