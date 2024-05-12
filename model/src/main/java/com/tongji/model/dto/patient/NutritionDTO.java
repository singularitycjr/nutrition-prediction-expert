package com.tongji.model.dto.patient;

import lombok.Data;

import java.util.List;

@Data
public class NutritionDTO {
    Integer dietId;
    @Data
    public static class Food {
        Integer food;
        String region;
        String name;
    }
    List<Food> foods;
}
