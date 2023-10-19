package com.tongji.model.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FoodQuery extends PageQuery {
    String name;
    String en;
}
