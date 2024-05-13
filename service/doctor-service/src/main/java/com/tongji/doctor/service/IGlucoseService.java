package com.tongji.doctor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.dto.common.GlucoseFileAddDTO;
import com.tongji.model.dto.doctor.TimeRangeDTO;
import com.tongji.model.pojo.Glucose;
import com.tongji.model.vo.ResponseResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface IGlucoseService extends IService<Glucose> {

    ResponseResult getGlucose(TimeRangeDTO timeRangeDTO);
    ResponseResult uploadGlucoseFile(Long patientId,MultipartFile file);
    ResponseResult addFileData( GlucoseFileAddDTO glucoseFileAddDTO);
    ResponseResult getPredictGlucose( Long patientId);


}
