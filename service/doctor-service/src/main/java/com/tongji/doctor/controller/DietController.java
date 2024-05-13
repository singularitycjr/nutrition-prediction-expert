package com.tongji.doctor.controller;

import com.tongji.doctor.service.IRecordDetailService;
import com.tongji.doctor.service.IRecordService;
import com.tongji.model.dto.doctor.TimeRangeDTO;
import com.tongji.model.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diet")
@Tag(name = "饮食数据")
public class DietController {
    @Autowired
    private IRecordService recordService;

    @Autowired
    private IRecordDetailService recordDetailService;

    @GetMapping("/statisticByDay")
    @Operation(summary = "按天统计营养摄入信息")
    public ResponseResult statisticByDay(TimeRangeDTO timeRangeDTO){
        return recordService.statisticByDay(timeRangeDTO);
    }

    @GetMapping("/getRecord")
    @Operation(summary = "获取饮食记录")
    public ResponseResult getRecord(TimeRangeDTO timeRangeDTO){
        return recordService.getRecord(timeRangeDTO);
    }

    @GetMapping("/getDetailByRecordId")
    @Operation(summary = "获取饮食详细记录")
    public ResponseResult getDetailByRecordId(@PathVariable("id") Long id){
        return recordService.getDetailByRecordId(id);
    }
}
