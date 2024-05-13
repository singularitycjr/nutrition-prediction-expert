package com.tongji.doctor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.dto.doctor.TimeRangeDTO;
import com.tongji.model.vo.ResponseResult;
import com.tongji.model.pojo.Record;
import org.springframework.web.bind.annotation.PathVariable;

public interface IRecordService extends IService<Record> {
    ResponseResult statisticByDay(TimeRangeDTO timeRangeDTO);
    ResponseResult getRecord(TimeRangeDTO timeRangeDTO);
    ResponseResult getDetailByRecordId( Long id);
}
