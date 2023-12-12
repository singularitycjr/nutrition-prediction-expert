package com.tongji.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.dto.GlucoseDTO;
import com.tongji.model.dto.TimeRangeDTO;
import com.tongji.model.pojo.Glucose;
import com.tongji.model.vo.ResponseResult;

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

    ResponseResult addGlucose(GlucoseDTO glucoseDTO);
}
