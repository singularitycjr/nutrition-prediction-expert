package com.tongji.doctor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.dto.doctor.PatientQueryDTO;
import com.tongji.model.pojo.Doctor;
import com.tongji.model.pojo.User;
import com.tongji.model.vo.ResponseResult;

public interface IPatientInfoService extends IService<User> {
    ResponseResult getAll();

    ResponseResult getByProperty(PatientQueryDTO patientQueryDTO);

}
