package com.tongji.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FoodAddDTO {
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
}
