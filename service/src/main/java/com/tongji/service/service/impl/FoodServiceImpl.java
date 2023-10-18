package com.tongji.service.service.impl;

import com.tongji.model.pojos.Food;
import com.tongji.service.mapper.FoodMapper;
import com.tongji.service.service.IFoodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 食物信息 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-10-18
 */
@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements IFoodService {

}
