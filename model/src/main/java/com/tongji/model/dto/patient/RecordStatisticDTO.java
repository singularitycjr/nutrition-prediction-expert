package com.tongji.model.dto.patient;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecordStatisticDTO {

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

    private BigDecimal breakfastCalorieMass;
    private BigDecimal lunchCalorieMass;
    private BigDecimal dinnerCalorieMass;
    private BigDecimal snackCalorieMass;

    public RecordStatisticDTO() {
        carbohydrateMass= new BigDecimal(0);
        fatMass= new BigDecimal(0);
        proteinMass= new BigDecimal(0);
        celluloseMass= new BigDecimal(0);
        calorieMass= new BigDecimal(0);
        breakfastCalorieMass= new BigDecimal(0);
        lunchCalorieMass= new BigDecimal(0);
        dinnerCalorieMass= new BigDecimal(0);
        snackCalorieMass= new BigDecimal(0);
    }
}
