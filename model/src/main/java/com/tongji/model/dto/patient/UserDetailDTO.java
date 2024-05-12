package com.tongji.model.dto.patient;

import lombok.Data;

@Data
public class UserDetailDTO {

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
}
