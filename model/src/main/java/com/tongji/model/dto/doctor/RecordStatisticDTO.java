package com.tongji.model.dto.doctor;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecordStatisticDTO {

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
}
