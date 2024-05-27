package com.tongji.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.dto.patient.UserLoginDTO;
import com.tongji.model.dto.patient.UserDTO;
import com.tongji.model.dto.patient.UserDetailDTO;
import com.tongji.model.pojo.User;
import com.tongji.model.vo.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 员工信息 服务类
 * </p>
 *
 * @author author
 * @since 2023-10-18
 */
public interface IUserService extends IService<User> {
    ResponseResult getUser();

    ResponseResult login(UserLoginDTO userLoginDTO);

    ResponseResult logout();

    ResponseResult register(UserDTO dto);

    ResponseResult sendCode(String mobile);

    ResponseResult updateName(UserDTO dto);

    ResponseResult updatePassword(UserDTO dto);

    ResponseResult sendCodeUpdate(String mobile);

    ResponseResult forgetPassword(UserDTO dto);

    ResponseResult getDetail();

    ResponseResult updateDetail(UserDetailDTO userDetailDTO);

    ResponseResult uploadProfix( MultipartFile picture);

    ResponseResult getDoctor();
}
