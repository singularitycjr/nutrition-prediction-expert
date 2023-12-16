package com.tongji.service.service;

import com.tongji.model.pojo.FoodCategory;
import com.tongji.model.vo.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 食物分类 服务类
 * </p>
 *
 * @author author
 * @since 2023-12-09
 */
public interface IFoodCategoryService extends IService<FoodCategory> {

    ResponseResult getAll();

    ResponseResult getById(Long id);
}
