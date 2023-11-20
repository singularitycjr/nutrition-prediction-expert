package com.tongji.service.controller;


import com.tongji.model.query.FoodQuery;
import com.tongji.model.vo.ResponseResult;
import com.tongji.service.service.IFoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 食物信息 前端控制器
 * </p>
 *
 * @author author
 * @since 2023-10-18
 */
@RestController
@RequestMapping("/food")
@Slf4j
@Tag(name = "食物信息")
public class FoodController {

    @Autowired
    private IFoodService foodService;

    @GetMapping("/{id}")
    @Operation(summary = "根据id查询食物信息")
    public ResponseResult getById(@PathVariable("id") Long id) {
        return ResponseResult.okResult(this.foodService.getById(id));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询食物信息")
    public ResponseResult page(FoodQuery foodQuery) {
        log.info("分页查询食物信息: {}", foodQuery);
        return this.foodService.queryPage(foodQuery);
    }

}
