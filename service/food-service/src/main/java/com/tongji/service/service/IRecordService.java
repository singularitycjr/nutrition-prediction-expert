package com.tongji.service.service;

import com.tongji.model.dto.patient.*;
import com.tongji.model.pojo.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.vo.ResponseResult;

/**
 * <p>
 * 预测信息 服务类
 * </p>
 *
 * @author author
 * @since 2023-11-29
 */
public interface IRecordService extends IService<Record> {

    ResponseResult getRecord(TimeRangeDTO timeRangeDTO);

    ResponseResult deleteRecord(Long id);

    ResponseResult updateRecord(RecordDTO recordDTO);

    ResponseResult addRecord(RecordAddDTO recordAddDTO);


    ResponseResult recognize(RecognizeDTO recognizeDTO);

    ResponseResult nutrition(FoodChosenDTO foodChosenDTO);

    ResponseResult statistic(TimeRangeDTO timeRangeDTO);

    ResponseResult segrec(SegRecDTO segRecDTO);
    ResponseResult nutrition(NutritionDTO nutritionDTO);
}
