package com.tongji.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.dto.doctor.DoctorDTO;
import com.tongji.model.dto.common.DoctorDetailDTO;
import com.tongji.model.dto.doctor.DoctorLoginDTO;
import com.tongji.model.pojo.Doctor;
import com.tongji.model.vo.ResponseResult;
import org.springframework.web.bind.annotation.PathVariable;
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

    ResponseResult getDoctorById(Long id);

    ResponseResult login(DoctorLoginDTO userLoginDTO);

    ResponseResult logout();

    ResponseResult register(DoctorDTO dto);

    ResponseResult sendCode(String mobile);

    ResponseResult updateName(DoctorDTO dto);

    ResponseResult updatePassword(DoctorDTO dto);

    ResponseResult sendCodeUpdate(String mobile);

    ResponseResult forgetPassword(DoctorDTO dto);

    ResponseResult getDetail();

    ResponseResult updateDetail(DoctorDetailDTO doctorDetailDTO);

    ResponseResult uploadProfix( MultipartFile picture);
}
