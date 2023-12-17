package com.tongji.model.json;

import lombok.Data;

import java.util.List;

@Data
public class FoodChoices {
    @Data
    public static class result {
        private Integer food;
        private List<String> top5;
    }

    private List<result> results;
}
