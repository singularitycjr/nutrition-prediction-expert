package com.tongji.model.pojo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 食物信息
 * </p>
 *
 * @author author
 * @since 2023-12-12
 */
@TableName("food")
public class Food implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 英文名
     */
    private String en;

    /**
     * 密度(g/ml)
     */
    private BigDecimal density;

    /**
     * 碳水化合物质量密度(g/g)
     */
    private BigDecimal carbohydrateMassDensity;

    /**
     * 碳水化合物体积密度(g/ml)
     */
    private BigDecimal carbohydrateVolumeDensity;

    /**
     * 脂肪质量密度(g/g)
     */
    private BigDecimal fatMassDensity;

    /**
     * 脂肪体积密度(g/ml)
     */
    private BigDecimal fatVolumeDensity;

    /**
     * 蛋白质质量密度(g/g)
     */
    private BigDecimal proteinMassDensity;

    /**
     * 蛋白质体积密度(g/ml)
     */
    private BigDecimal proteinVolumeDensity;

    /**
     * 纤维素质量密度(g/g)
     */
    private BigDecimal celluloseMassDensity;

    /**
     * 纤维素体积密度(g/ml)
     */
    private BigDecimal celluloseVolumeDensity;

    /**
     * 热量质量密度(kcal/g)
     */
    private BigDecimal calorieMassDensity;

    /**
     * 热量体积密度(kcal/ml)
     */
    private BigDecimal calorieVolumeDensity;

    /**
     * 分类ID
     */
    private Long category;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }
    public BigDecimal getDensity() {
        return density;
    }

    public void setDensity(BigDecimal density) {
        this.density = density;
    }
    public BigDecimal getCarbohydrateMassDensity() {
        return carbohydrateMassDensity;
    }

    public void setCarbohydrateMassDensity(BigDecimal carbohydrateMassDensity) {
        this.carbohydrateMassDensity = carbohydrateMassDensity;
    }
    public BigDecimal getCarbohydrateVolumeDensity() {
        return carbohydrateVolumeDensity;
    }

    public void setCarbohydrateVolumeDensity(BigDecimal carbohydrateVolumeDensity) {
        this.carbohydrateVolumeDensity = carbohydrateVolumeDensity;
    }
    public BigDecimal getFatMassDensity() {
        return fatMassDensity;
    }

    public void setFatMassDensity(BigDecimal fatMassDensity) {
        this.fatMassDensity = fatMassDensity;
    }
    public BigDecimal getFatVolumeDensity() {
        return fatVolumeDensity;
    }

    public void setFatVolumeDensity(BigDecimal fatVolumeDensity) {
        this.fatVolumeDensity = fatVolumeDensity;
    }
    public BigDecimal getProteinMassDensity() {
        return proteinMassDensity;
    }

    public void setProteinMassDensity(BigDecimal proteinMassDensity) {
        this.proteinMassDensity = proteinMassDensity;
    }
    public BigDecimal getProteinVolumeDensity() {
        return proteinVolumeDensity;
    }

    public void setProteinVolumeDensity(BigDecimal proteinVolumeDensity) {
        this.proteinVolumeDensity = proteinVolumeDensity;
    }
    public BigDecimal getCelluloseMassDensity() {
        return celluloseMassDensity;
    }

    public void setCelluloseMassDensity(BigDecimal celluloseMassDensity) {
        this.celluloseMassDensity = celluloseMassDensity;
    }
    public BigDecimal getCelluloseVolumeDensity() {
        return celluloseVolumeDensity;
    }

    public void setCelluloseVolumeDensity(BigDecimal celluloseVolumeDensity) {
        this.celluloseVolumeDensity = celluloseVolumeDensity;
    }
    public BigDecimal getCalorieMassDensity() {
        return calorieMassDensity;
    }

    public void setCalorieMassDensity(BigDecimal calorieMassDensity) {
        this.calorieMassDensity = calorieMassDensity;
    }
    public BigDecimal getCalorieVolumeDensity() {
        return calorieVolumeDensity;
    }

    public void setCalorieVolumeDensity(BigDecimal calorieVolumeDensity) {
        this.calorieVolumeDensity = calorieVolumeDensity;
    }
    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", name=" + name +
                ", en=" + en +
                ", density=" + density +
                ", carbohydrateMassDensity=" + carbohydrateMassDensity +
                ", carbohydrateVolumeDensity=" + carbohydrateVolumeDensity +
                ", fatMassDensity=" + fatMassDensity +
                ", fatVolumeDensity=" + fatVolumeDensity +
                ", proteinMassDensity=" + proteinMassDensity +
                ", proteinVolumeDensity=" + proteinVolumeDensity +
                ", celluloseMassDensity=" + celluloseMassDensity +
                ", celluloseVolumeDensity=" + celluloseVolumeDensity +
                ", calorieMassDensity=" + calorieMassDensity +
                ", calorieVolumeDensity=" + calorieVolumeDensity +
                ", category=" + category +
                ", userId=" + userId +
                "}";
    }
}
