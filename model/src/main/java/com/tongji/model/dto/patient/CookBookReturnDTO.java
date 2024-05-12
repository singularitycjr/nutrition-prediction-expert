package com.tongji.model.dto.patient;

import lombok.Data;

import java.util.*;

@Data
public class CookBookReturnDTO {
    private String energy;

    private String proteinMass;

    private String proteinEnergyRatio;

    private String carbohydrateMass;

    private String carbohydrateEnergyRatio;

    private String fatMass;

    private String fatEnergyRatio;

    private String note;

    private String oilSalt;

    private List<OtherFoodDTO> foods;

    public CookBookReturnDTO() {
        foods = new ArrayList<>();
    }

}
