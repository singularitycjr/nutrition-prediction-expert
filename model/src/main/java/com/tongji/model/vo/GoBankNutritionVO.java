package com.tongji.model.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public GoBankNutritionFormatVO format() {
        GoBankNutritionFormatVO goBankNutritionFormatVO = new GoBankNutritionFormatVO();
        goBankNutritionFormatVO.setRequestId(this.requestId);
        goBankNutritionFormatVO.setCode(this.code);

        GoBankNutritionFormatVO.Message message = new GoBankNutritionFormatVO.Message();
        List<GoBankNutritionFormatVO.Result> results = new ArrayList<>();
        for (Result result : this.message.results ) {
            GoBankNutritionFormatVO.Result result1 = new GoBankNutritionFormatVO.Result();
            result1.setName(result.getName());
            for (Nutrition nutrition : result.getNutrition()) {
                String str = nutrition.getValue();
                str = str.replaceAll("[a-zA-Z]+$", "");
                Float value = Float.parseFloat(str);

                String key = nutrition.getKey();
                if (Objects.equals(key, "质量")) {
                    result1.setFoodMass(value);
                } else if (Objects.equals(key, "热量")) {
                    result1.setCalorieMass(value);
                } else if (Objects.equals(key, "碳水化合物")) {
                    result1.setCarbohydrateMass(value);
                } else if (Objects.equals(key, "脂肪")) {
                    result1.setFatMass(value);
                } else if (Objects.equals(key, "蛋白质")) {
                    result1.setProteinMass(value);
                }
            }
            results.add(result1);
        }
        message.setResults(results);
        goBankNutritionFormatVO.setMessage(message);
        return goBankNutritionFormatVO;
    }
}
