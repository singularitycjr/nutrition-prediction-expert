package com.tongji.model.dto.common;

import lombok.Data;

@Data
public class DoctorDetailDTO {

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
     * 部门
     * */
    private String department;

    /**
     * 职称头衔
     * */
    private String title;

    /**
     * 简介
     * */
    private String introduction;
}
