package com.tongji.doctor.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patientInfo")
@Tag(name = "患者数据")
public class PatientInfoController {

}
