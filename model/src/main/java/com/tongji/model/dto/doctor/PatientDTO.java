package com.tongji.model.dto.doctor;

import lombok.Data;

@Data
public class PatientDTO {
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

    /**
     * 年龄
     */
    private Integer age;

    /**
     * cm
     */
    private Double height;

    /**
     * kg
     */
    private Double weight;

    /**
     * 糖尿病类型（Ⅰ型糖尿病、Ⅱ型糖尿病、妊娠期糖尿病）
     */
    private String diabetesType;

    /**
    * 头像
    */
    private String profile;

    /**
     * 关联医生id
     */
    private Long doctor;
}
