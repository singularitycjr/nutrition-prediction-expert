package com.tongji.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.dto.patient.FoodAddDTO;
import com.tongji.model.dto.patient.FoodDTO;
import com.tongji.model.pojo.Food;
import com.tongji.model.query.FoodQuery;
import com.tongji.model.vo.ResponseResult;

/**
 * <p>
 * 食物信息 服务类
 * </p>
 *
 * @author author
 * @since 2023-10-18
 */
public interface IFoodService extends IService<Food> {

    ResponseResult queryPage(FoodQuery foodQuery);

    ResponseResult getById(Long id);

    ResponseResult deleteFood(Long id);

    ResponseResult updateFood(FoodDTO foodDTO);

    ResponseResult addFood(FoodAddDTO foodAddDTO);

//    ResponseResult getByName(String name);
    Long getIdByName(String name);
}
