package com.tongji.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.dto.patient.GlucoseAddDTO;
import com.tongji.model.dto.patient.GlucoseDTO;
import com.tongji.model.dto.patient.GlucoseFileAddDTO;
import com.tongji.model.dto.patient.TimeRangeDTO;
import com.tongji.model.pojo.Glucose;
import com.tongji.model.vo.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2023-11-29
 */
public interface IGlucoseService extends IService<Glucose> {

    ResponseResult getGlucose(TimeRangeDTO timeRangeDTO);

    ResponseResult deleteGlucose(Long id);

    ResponseResult updateGlucose(GlucoseDTO glucoseDTO);

    ResponseResult addGlucose(GlucoseAddDTO glucoseAddDTO);

    ResponseResult uploadGlucoseFile(MultipartFile file) ;

    ResponseResult addFileData(GlucoseFileAddDTO glucoseFileAddDTO);

    ResponseResult getPredictGlucose();
}
