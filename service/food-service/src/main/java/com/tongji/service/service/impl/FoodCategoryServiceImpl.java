package com.tongji.service.service.impl;

import com.tongji.model.pojo.FoodCategory;
import com.tongji.model.vo.ResponseResult;
import com.tongji.service.mapper.FoodCategoryMapper;
import com.tongji.service.service.IFoodCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 食物分类 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-12-09
 */
@Service
public class FoodCategoryServiceImpl extends ServiceImpl<FoodCategoryMapper, FoodCategory> implements IFoodCategoryService {

    @Override
    public ResponseResult getAll() {
        return ResponseResult.okResult(this.baseMapper.getAll());
    }

    @Override
    public ResponseResult getById(Long id) {
        return ResponseResult.okResult(this.baseMapper.selectById(id));
    }
}
