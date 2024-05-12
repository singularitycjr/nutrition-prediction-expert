package com.tongji.model.dto.patient;

import lombok.Data;

import java.util.List;

@Data
public class FoodChosenDTO {

    @Data
    public static class food {
        private Integer num;
        private String name;
    }

    private List<food> foods;
}
