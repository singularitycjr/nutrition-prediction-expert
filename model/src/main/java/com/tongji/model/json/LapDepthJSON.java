package com.tongji.model.json;

import lombok.Data;

import java.util.List;

@Data
public class LapDepthJSON {
    // 内部类
    @Data
    public static class Nutrition {
        private String key;
        private String value;

    }

    @Data
    public static class Result {
        private Long id;
        private String name;
        private List<Nutrition> nutrition;

    }

    private List<Result> results;
}
