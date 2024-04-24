package com.tongji.service.controller;


import cn.dev33.satoken.annotation.SaCheckRole;
import com.tongji.model.dto.RecordDetailAddDTO;
import com.tongji.model.dto.RecordDetailDTO;
import com.tongji.model.dto.TimeRangeDTO;
import com.tongji.model.vo.ResponseResult;
import com.tongji.service.service.IRecordDetailService;
import com.tongji.service.service.IRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 预测信息细节 前端控制器
 * </p>
 *
 * @author author
 * @since 2023-11-29
 */
@RestController
//@SaCheckRole("PATIENT")
@RequestMapping("/record-detail")
@Slf4j
@Tag(name = "食物记录详细信息")
public class RecordDetailController {

    @Autowired
    private IRecordDetailService recordDetailService;

    @Operation(summary = "通过id获取饮食详细数据")
    @GetMapping("/getById/{id}")
    public ResponseResult getById(@PathVariable("id") Long id){
        return this.recordDetailService.getById(id);
    }

    @Operation(summary = "通过record_id获取饮食详细数据")
    @GetMapping("/getByRecordId/{record_id}")
    public ResponseResult getByRecordId(@PathVariable("record_id") Long record_id){
        return this.recordDetailService.getByRecordId(record_id);
    }

    @Operation(summary = "删除饮食详细数据")
    @DeleteMapping("/deleteRecordDetail/{id}")
    public ResponseResult deleteRecordDetail(@PathVariable("id") Long id){
        return this.recordDetailService.deleteRecordDetail(id);
    }

    @Operation(summary = "修改饮食详细数据")
    @PutMapping("/updateRecordDetail")
    public ResponseResult updateRecordDetail(@RequestBody RecordDetailDTO recordDetailDTO){
        return this.recordDetailService.updateRecordDetail(recordDetailDTO);
    }

    @Operation(summary = "添加饮食详细数据")
    @PostMapping("/addRecordDetail")
    public ResponseResult addRecordDetail(@RequestBody RecordDetailAddDTO recordDetailAddDTO){
        return this.recordDetailService.addRecordDetail(recordDetailAddDTO);
    }

}
