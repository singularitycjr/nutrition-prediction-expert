package com.tongji.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class GoBankNutritionVO {
    @Data
    public static class Nutrition {
        String key;
        String value;
    }
    @Data
    public static class Result {
        String name;
        List<Nutrition> nutrition;
    }
    @Data
    public static class Message {
        List<Result> results;
    }
    String requestId;
    Integer code;
    Message message;
}
