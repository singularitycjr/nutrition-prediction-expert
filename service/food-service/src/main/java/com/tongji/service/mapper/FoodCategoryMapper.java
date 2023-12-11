package com.tongji.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tongji.model.pojo.FoodCategory;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 食物分类 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-12-09
 */
public interface FoodCategoryMapper extends BaseMapper<FoodCategory> {
    @Select("select * from food_category")
    List<FoodCategory> getAll();
}
