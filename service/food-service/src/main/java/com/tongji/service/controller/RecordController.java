package com.tongji.service.controller;


import com.tongji.model.dto.*;
import com.tongji.model.vo.ResponseResult;
import com.tongji.service.service.IRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 预测信息 前端控制器
 * </p>
 *
 * @author author
 * @since 2023-11-29
 */
@RestController
@RequestMapping("/record")
@Slf4j
@Tag(name = "食物记录信息")
public class RecordController {
    @Autowired
    private IRecordService recordService;

    @Operation(summary = "获取饮食数据")
    @GetMapping("/getRecord")
    public ResponseResult getRecord(TimeRangeDTO timeRangeDTO){
        log.info("获取饮食数据: {}", timeRangeDTO);
        return this.recordService.getRecord(timeRangeDTO);
    }

    @Operation(summary = "删除饮食数据")
    @DeleteMapping("/deleteRecord/{id}")
    public ResponseResult deleteRecord(@PathVariable("id") Long id){
        return this.recordService.deleteRecord(id);
    }

    @Operation(summary = "修改饮食数据")
    @PutMapping("/updateRecord")
    public ResponseResult updateRecord(@RequestBody RecordDTO recordDTO){
        return this.recordService.updateRecord(recordDTO);
    }

    @Operation(summary = "添加饮食数据")
    @PostMapping("/addRecord")
    public ResponseResult addRecord(@RequestBody RecordAddDTO recordAddDTO){
        return this.recordService.addRecord(recordAddDTO);
    }

    //@Operation(summary = "识别饮食信息")
    //@GetMapping("/recognize")
    //public ResponseResult recognize(RecognizeDTO recognizeDTO){
    //    return this.recordService.recognize(recognizeDTO);
    //}
    //
    //@Operation(summary = "营养评估")
    //@PostMapping("/nutrition")
    //public ResponseResult nutrition(@RequestBody FoodChosenDTO foodChosenDTO){
    //    return this.recordService.nutrition(foodChosenDTO);
    //}

    @Operation(summary = "时段营养统计")
    @GetMapping("/statistic")
    public ResponseResult statistic(TimeRangeDTO timeRangeDTO){
        return this.recordService.statistic(timeRangeDTO);
    }

    @Operation(summary = "调用GO后端SegRec接口")
    @PostMapping("/segrec")
    public ResponseResult segrec(@RequestBody SegRecDTO segRecDTO){
        return this.recordService.segrec(segRecDTO);
    }

    @Operation(summary = "调用GO后端Nutrition接口")
    @PostMapping("/nutrition")
    public ResponseResult nutrition(@RequestBody NutritionDTO nutritionDTO){
        return this.recordService.nutrition(nutritionDTO);
    }
}
