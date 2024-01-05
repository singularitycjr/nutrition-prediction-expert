package com.tongji.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.dto.LoginDTO;
import com.tongji.model.dto.UserDTO;
import com.tongji.model.dto.UserDetailDTO;
import com.tongji.model.pojo.User;
import com.tongji.model.pojo.UserDetail;
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

    ResponseResult updateName(UserDTO dto);

    ResponseResult updatePassword(UserDTO dto);

    ResponseResult sendCodeUpdate(String mobile);

    ResponseResult forgetPassword(UserDTO dto);

    ResponseResult getDetail();

    ResponseResult updateDetail(UserDetailDTO userDetailDTO);
}
