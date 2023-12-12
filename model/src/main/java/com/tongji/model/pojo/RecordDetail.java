package com.tongji.model.pojo;

import java.math.BigDecimal;
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
 * @since 2023-12-12
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
    private Long foodId;

    /**
     * 食物质量(g)
     */
    private BigDecimal foodMass;

    /**
     * 碳水化合物质量(g)
     */
    private BigDecimal carbohydrateMass;

    /**
     * 脂肪质量(g)
     */
    private BigDecimal fatMass;

    /**
     * 蛋白质质量(g)
     */
    private BigDecimal proteinMass;

    /**
     * 纤维素质量(g)
     */
    private BigDecimal celluloseMass;

    /**
     * 热量(kcal)
     */
    private BigDecimal calorieMass;

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
    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }
    public BigDecimal getFoodMass() {
        return foodMass;
    }

    public void setFoodMass(BigDecimal foodMass) {
        this.foodMass = foodMass;
    }
    public BigDecimal getCarbohydrateMass() {
        return carbohydrateMass;
    }

    public void setCarbohydrateMass(BigDecimal carbohydrateMass) {
        this.carbohydrateMass = carbohydrateMass;
    }
    public BigDecimal getFatMass() {
        return fatMass;
    }

    public void setFatMass(BigDecimal fatMass) {
        this.fatMass = fatMass;
    }
    public BigDecimal getProteinMass() {
        return proteinMass;
    }

    public void setProteinMass(BigDecimal proteinMass) {
        this.proteinMass = proteinMass;
    }
    public BigDecimal getCelluloseMass() {
        return celluloseMass;
    }

    public void setCelluloseMass(BigDecimal celluloseMass) {
        this.celluloseMass = celluloseMass;
    }
    public BigDecimal getCalorieMass() {
        return calorieMass;
    }

    public void setCalorieMass(BigDecimal calorieMass) {
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
