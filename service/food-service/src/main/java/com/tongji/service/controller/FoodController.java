package com.tongji.service.controller;


import cn.dev33.satoken.annotation.SaCheckRole;
import com.tongji.model.dto.FoodAddDTO;
import com.tongji.model.dto.FoodDTO;
import com.tongji.model.dto.GlucoseDTO;
import com.tongji.model.pojo.Glucose;
import com.tongji.model.query.FoodQuery;
import com.tongji.model.vo.ResponseResult;
import com.tongji.service.service.IFoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 食物信息 前端控制器
 * </p>
 *
 * @author author
 * @since 2023-10-18
 */
@RestController
//@SaCheckRole("PATIENT")
@RequestMapping("/food")
@Tag(name = "食物信息")
@Slf4j
public class FoodController {

    @Autowired
    private IFoodService foodService;

    @GetMapping("/{id}")
    @Operation(summary = "根据id查询食物信息")
    public ResponseResult getById(@PathVariable("id") Long id) {
        return this.foodService.getById(id);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询食物信息")
    public ResponseResult page(FoodQuery foodQuery) {
        log.info("分页查询食物信息: {}", foodQuery);
        return this.foodService.queryPage(foodQuery);
    }

//    @GetMapping("/getByName/{name}")
//    @Operation(summary = "根据名称分页查询食物信息")
//    public ResponseResult getByName(@PathVariable("name") String name) {
//        return this.foodService.getByName(name);
//    }
//
//    @GetMapping("/getByCategory/")
//    @Operation(summary = "根据类别查询食物信息")
//    public ResponseResult getByCategory(@PathVariable("name") String name) {
//        log.info("分页查询食物信息: {}", name);
//        return this.foodService.getByName(name);
//    }

    @Operation(summary = "删除食物数据")
    @DeleteMapping("/deleteFood/{id}")
    public ResponseResult deleteFood(@PathVariable("id") Long id){
        return this.foodService.deleteFood(id);
    }

    @Operation(summary = "修改食物数据")
    @PutMapping("/updateFood")
    public ResponseResult updateFood(@RequestBody FoodDTO foodDTO){
        return this.foodService.updateFood(foodDTO);
    }

    @Operation(summary = "添加食物数据")
    @PostMapping("/addFood")
    public ResponseResult addFood(@RequestBody FoodAddDTO foodAddDTO){
        return this.foodService.addFood(foodAddDTO);
    }
}
