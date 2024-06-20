package com.tongji.doctor.controller;


import com.tongji.doctor.service.Impl.GlucoseServiceImpl;
import com.tongji.model.dto.common.GlucoseFileAddDTO;
import com.tongji.model.dto.doctor.TimeRangeDTO;
import com.tongji.model.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/glucose")
@Tag(name = "患者血糖数据")
public class GlucoseController {
    @Autowired
    GlucoseServiceImpl glucoseService;

    @Operation(summary = "获取血糖数据")
    @GetMapping("/getGlucose")
    public ResponseResult getGlucose( TimeRangeDTO timeRangeDTO){
        log.info("获取血糖数据: {}", timeRangeDTO);
        return this.glucoseService.getGlucose(timeRangeDTO);
    }

//    @Operation(summary = "删除血糖数据")
//    @DeleteMapping("/deleteGlucose/{id}")
//    public ResponseResult deleteGlucose(@PathVariable("id") Long id){
//        return this.glucoseService.deleteGlucose(id);
//    }
//
//    @Operation(summary = "修改血糖数据")
//    @PutMapping("/updateGlucose")
//    public ResponseResult updateGlucose(@RequestBody GlucoseDTO glucoseDTO){
//        return this.glucoseService.updateGlucose(glucoseDTO);
//    }
//
//    @Operation(summary = "添加血糖数据")
//    @PostMapping("/addGlucose")
//    public ResponseResult addGlucose(@RequestBody GlucoseAddDTO glucoseAddDTO){
//        return this.glucoseService.addGlucose(glucoseAddDTO);
//    }

    @Operation(summary = "添加血糖数据文件（传患者id和文件）")
    @PostMapping("/uploadGlucoseFile")
    public ResponseResult uploadGlucoseFile(@RequestParam("id") Long patientId,@RequestParam("file") MultipartFile file){
        return this.glucoseService.uploadGlucoseFile(patientId,file);
    }

//    @Operation(summary = "上传血糖两列数据")
//    @PostMapping("/addFileData")
//    public ResponseResult addFileData(@RequestBody GlucoseFileAddDTO glucoseFileAddDTO){
//        return this.glucoseService.addFileData(glucoseFileAddDTO);
//    }

    @Operation(summary = "调go接口获取血糖预测结果（传患者id）")
    @GetMapping("/getPredictGlucose/{id}")
    public ResponseResult getPredictGlucose(@PathVariable("id") Long patientId){
        return this.glucoseService.getPredictGlucose(patientId);
    }
}
