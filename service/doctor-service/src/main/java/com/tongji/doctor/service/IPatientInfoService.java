package com.tongji.doctor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.pojo.User;
import com.tongji.model.query.PageQuery;
import com.tongji.model.query.PatientQuery;
import com.tongji.model.vo.ResponseResult;

public interface IPatientInfoService extends IService<User> {
    ResponseResult getAll(PatientQuery patientQuery);

    ResponseResult getById(Long id);

    ResponseResult getOwnPatients(PatientQuery patientQuery);

}
