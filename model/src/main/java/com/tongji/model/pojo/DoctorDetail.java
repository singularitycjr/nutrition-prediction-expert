package com.tongji.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("doctor_detail")
public class DoctorDetail implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long doctorId;

    private String gender;

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
