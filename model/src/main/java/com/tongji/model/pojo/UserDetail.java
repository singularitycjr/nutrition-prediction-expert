package com.tongji.model.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author author
 * @since 2023-12-12
 */
@TableName("user_detail")
public class UserDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String gender;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
    public String getDiabetesType() {
        return diabetesType;
    }

    public void setDiabetesType(String diabetesType) {
        this.diabetesType = diabetesType;
    }

    @Override
    public String toString() {
        return "UserDetail{" +
                "id=" + id +
                ", userId=" + userId +
                ", gender=" + gender +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", diabetesType=" + diabetesType +
                "}";
    }
}
