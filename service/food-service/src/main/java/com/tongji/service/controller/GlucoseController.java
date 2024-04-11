package com.tongji.service.controller;


import com.tongji.model.dto.GlucoseAddDTO;
import com.tongji.model.dto.GlucoseDTO;
import com.tongji.model.dto.GlucoseFileAddDTO;
import com.tongji.model.dto.TimeRangeDTO;
import com.tongji.model.vo.ResponseResult;
import com.tongji.service.service.IGlucoseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2023-11-29
 */
@RestController
@RequestMapping("/glucose")
@Slf4j
@Tag(name = "血糖数据")
public class GlucoseController {

    @Autowired
    private IGlucoseService glucoseService;

    @Operation(summary = "获取血糖数据")
    @GetMapping("/getGlucose")
    public ResponseResult getGlucose(TimeRangeDTO timeRangeDTO){
        log.info("获取血糖数据: {}", timeRangeDTO);
        return this.glucoseService.getGlucose(timeRangeDTO);
    }

    @Operation(summary = "删除血糖数据")
    @DeleteMapping("/deleteGlucose/{id}")
    public ResponseResult deleteGlucose(@PathVariable("id") Long id){
        return this.glucoseService.deleteGlucose(id);
    }

    @Operation(summary = "修改血糖数据")
    @PutMapping("/updateGlucose")
    public ResponseResult updateGlucose(@RequestBody GlucoseDTO glucoseDTO){
        return this.glucoseService.updateGlucose(glucoseDTO);
    }

    @Operation(summary = "添加血糖数据")
    @PostMapping("/addGlucose")
    public ResponseResult addGlucose(@RequestBody GlucoseAddDTO glucoseAddDTO){
        return this.glucoseService.addGlucose(glucoseAddDTO);
    }

    @Operation(summary = "添加血糖数据文件")
    @PostMapping("/uploadGlucoseFile")
    public ResponseResult uploadGlucoseFile(@RequestParam("file") MultipartFile file){
        return this.glucoseService.uploadGlucoseFile(file);
    }

    @Operation(summary = "上传血糖两列数据")
    @PostMapping("/addFileData")
    public ResponseResult addFileData(@RequestBody GlucoseFileAddDTO glucoseFileAddDTO){
        return this.glucoseService.addFileData(glucoseFileAddDTO);
    }
}
