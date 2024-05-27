package com.tongji.doctor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.pojo.User;
import com.tongji.model.query.PageQuery;
import com.tongji.model.query.PatientQuery;
import com.tongji.model.vo.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IPatientInfoService extends IService<User> {
    ResponseResult getAll(PatientQuery patientQuery);

    ResponseResult getById(Long id);

    ResponseResult getOwnPatients(PatientQuery patientQuery);

    ResponseResult addPatients( List<Long> idList);

    ResponseResult deletePatients( List<Long> idList);

}
