package com.tongji.doctor.controller;


import com.tongji.doctor.service.IPatientInfoService;
import com.tongji.model.dto.doctor.PatientQueryDTO;
import com.tongji.model.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patientInfo")
@Tag(name = "患者数据")
public class PatientInfoController {
    @Autowired
    private IPatientInfoService patientInfoService;

    @GetMapping("/getAll")
    @Operation(summary = "获取所有患者信息")
    public ResponseResult getAll(){
        return patientInfoService.getAll();
    }

    @GetMapping("/getById/{id}")
    @Operation(summary = "使用患者id获取患者信息")
    public ResponseResult getById(@PathVariable Long id){
        return patientInfoService.getById(id);
    }

    @GetMapping("/getByProperty")
    @Operation(summary = "按name/phone获取患者信息")
    public ResponseResult getByProperty(PatientQueryDTO patientQueryDTO){
        return patientInfoService.getByProperty(patientQueryDTO);
    }
}
