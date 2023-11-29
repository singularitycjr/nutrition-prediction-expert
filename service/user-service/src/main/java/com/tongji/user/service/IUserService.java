package com.tongji.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.dto.LoginDTO;
import com.tongji.model.dto.UserDTO;
import com.tongji.model.pojo.User;
import com.tongji.model.vo.ResponseResult;

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
