package com.tongji.model.dto.doctor;

import lombok.Data;

@Data
public class PatientBriefDTO {
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 账号
     */
    private String account;

    /**
     * 性别
     */
    private String gender;
}
