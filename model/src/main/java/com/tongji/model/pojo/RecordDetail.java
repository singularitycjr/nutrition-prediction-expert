package com.tongji.model.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 预测信息细节
 * </p>
 *
 * @author author
 * @since 2023-11-29
 */
@TableName("record_detail")
public class RecordDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 记录id
     */
    private Long recordId;

    /**
     * 食物id
     */
    private String foodId;

    /**
     * 食物质量(g)
     */
    private String foodMass;

    /**
     * 碳水化合物质量(g)
     */
    private String carbohydrateMass;

    /**
     * 脂肪质量(g)
     */
    private String fatMass;

    /**
     * 蛋白质质量(g)
     */
    private String proteinMass;

    /**
     * 纤维素质量(g)
     */
    private String celluloseMass;

    /**
     * 热量(kcal)
     */
    private String calorieMass;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }
    public String getFoodMass() {
        return foodMass;
    }

    public void setFoodMass(String foodMass) {
        this.foodMass = foodMass;
    }
    public String getCarbohydrateMass() {
        return carbohydrateMass;
    }

    public void setCarbohydrateMass(String carbohydrateMass) {
        this.carbohydrateMass = carbohydrateMass;
    }
    public String getFatMass() {
        return fatMass;
    }

    public void setFatMass(String fatMass) {
        this.fatMass = fatMass;
    }
    public String getProteinMass() {
        return proteinMass;
    }

    public void setProteinMass(String proteinMass) {
        this.proteinMass = proteinMass;
    }
    public String getCelluloseMass() {
        return celluloseMass;
    }

    public void setCelluloseMass(String celluloseMass) {
        this.celluloseMass = celluloseMass;
    }
    public String getCalorieMass() {
        return calorieMass;
    }

    public void setCalorieMass(String calorieMass) {
        this.calorieMass = calorieMass;
    }

    @Override
    public String toString() {
        return "RecordDetail{" +
            "id=" + id +
            ", recordId=" + recordId +
            ", foodId=" + foodId +
            ", foodMass=" + foodMass +
            ", carbohydrateMass=" + carbohydrateMass +
            ", fatMass=" + fatMass +
            ", proteinMass=" + proteinMass +
            ", celluloseMass=" + celluloseMass +
            ", calorieMass=" + calorieMass +
        "}";
    }
}
