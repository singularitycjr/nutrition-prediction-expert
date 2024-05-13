package com.tongji.doctor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.dto.doctor.TimeRangeDTO;
import com.tongji.model.vo.ResponseResult;

public interface IRecordService extends IService<Record> {
    ResponseResult statisticByDay(TimeRangeDTO timeRangeDTO);
}
