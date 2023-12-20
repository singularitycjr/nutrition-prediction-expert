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

        public Nutrition() {


        }
        public Nutrition(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    @Data
    public static class Result {
        private Long id;
        private String name;
        private List<Nutrition> nutrition;

        public Result() {

        }
        public Result(Long id, String name, List<Nutrition> nutrition) {
            this.id = id;
            this.name = name;
            this.nutrition = nutrition;
        }
    }

    private List<Result> results;
}
