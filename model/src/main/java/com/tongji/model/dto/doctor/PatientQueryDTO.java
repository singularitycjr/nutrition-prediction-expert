package com.tongji.model.dto.doctor;

import lombok.Data;

@Data
public class PatientQueryDTO {
    //电话号码，即账号
    private String account;

    //姓名
    private String name;
}
