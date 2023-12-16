package com.tongji.service.controller;


import com.tongji.model.pojo.FoodCategory;
import com.tongji.model.vo.ResponseResult;
import com.tongji.service.service.IFoodCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 食物分类 前端控制器
 * </p>
 *
 * @author author
 * @since 2023-12-09
 */
@RestController
@RequestMapping("/food-category")
@Tag(name = "食物分类信息")
public class FoodCategoryController {

    @Autowired
    private IFoodCategoryService foodCategoryService;

    @GetMapping("/getall")
    @Operation(summary = "获取所有食物分类信息")
    public ResponseResult getAll(){
        return this.foodCategoryService.getAll();
    }

    @GetMapping("/getById/{id}")
    @Operation(summary = "按id获取食物分类名称")
    public ResponseResult getById(@PathVariable("id") Long id){
        return this.foodCategoryService.getById(id);
    }


}
