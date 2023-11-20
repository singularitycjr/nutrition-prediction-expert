package com.tongji.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tongji.model.pojo.Food;
import com.tongji.model.query.FoodQuery;
import com.tongji.model.vo.PageVO;
import com.tongji.model.vo.ResponseResult;
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

    @Override
    public ResponseResult queryPage(FoodQuery foodQuery) {
        Integer pageNo = foodQuery.getPageNo();
        Integer pageSize = foodQuery.getPageSize();
        if (pageNo == null || pageNo < 1) {
            pageNo = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 5;
        }
        String orderBy = foodQuery.getOrderBy();
        Boolean isAsc = foodQuery.getIsAsc();
        if (StrUtil.isBlank(orderBy)) {
            orderBy = "id";
        }
        if (isAsc == null) {
            isAsc = true;
        }

        Page<Food> page = new Page<>(pageNo, pageSize);
        page.addOrder(new OrderItem(orderBy, isAsc));

        page = lambdaQuery()
                .like(StrUtil.isNotBlank(foodQuery.getName()), Food::getName, foodQuery.getName())
                .like(StrUtil.isNotBlank(foodQuery.getEn()), Food::getEn, foodQuery.getEn())
                .page(page);

        PageVO<Food> pageVO = new PageVO<>();
        pageVO.setTotal(page.getTotal());
        pageVO.setPages(page.getPages());
        pageVO.setList(page.getRecords());

        return ResponseResult.okResult(pageVO);
    }
}
