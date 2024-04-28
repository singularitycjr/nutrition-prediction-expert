package com.tongji.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.dto.DoctorDTO;
import com.tongji.model.dto.DoctorLoginDTO;
import com.tongji.model.pojo.Doctor;
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
public interface IDoctorService extends IService<Doctor> {
    ResponseResult getDoctor();

    ResponseResult login(DoctorLoginDTO userLoginDTO);

    ResponseResult logout();

    ResponseResult register(DoctorDTO dto);

    ResponseResult sendCode(String mobile);

    ResponseResult updateName(DoctorDTO dto);

    ResponseResult updatePassword(DoctorDTO dto);

    ResponseResult sendCodeUpdate(String mobile);

    ResponseResult forgetPassword(DoctorDTO dto);


    ResponseResult uploadProfix( MultipartFile picture);
}
