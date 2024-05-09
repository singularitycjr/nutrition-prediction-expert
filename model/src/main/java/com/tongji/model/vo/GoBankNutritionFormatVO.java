package com.tongji.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class GoBankNutritionFormatVO {
    @Data
    public static class Result {
        String name;
        Long id;
        Float foodMass;
        Float calorieMass;
        Float carbohydrateMass;
        Float fatMass;
        Float proteinMass;
    }
    @Data
    public static class Message {
        List<Result> results;
    }
    String requestId;
    Integer code;
    Message message;
}
