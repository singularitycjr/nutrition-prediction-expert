package com.tongji.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tongji.model.dto.FoodAddDTO;
import com.tongji.model.dto.FoodDTO;
import com.tongji.model.pojo.Food;
import com.tongji.model.query.FoodQuery;
import com.tongji.model.vo.PageVO;
import com.tongji.model.vo.ResponseResult;
import com.tongji.service.mapper.FoodMapper;
import com.tongji.service.service.IFoodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.List;

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
//        LambdaQueryChainWrapper<Food> queryWrapper = lambdaQuery();
        Integer pageNo = foodQuery.getPageNo();
        Integer pageSize = foodQuery.getPageSize();
        Long userId = StpUtil.getLoginIdAsLong();
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
                .eq(ObjectUtil.isAllNotEmpty(foodQuery.getCategoryId()), Food::getCategory, foodQuery.getCategoryId())
                .and(q -> q.isNull(Food::getUserId).or().eq(Food::getUserId, userId))
                .page(page);

        PageVO<Food> pageVO = new PageVO<>();
        pageVO.setTotal(page.getTotal());
        pageVO.setPages(page.getPages());
        pageVO.setList(page.getRecords());

        return ResponseResult.okResult(pageVO);
    }

    @Override
    public ResponseResult getById(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Food food = this.getOne(
                Wrappers.<Food>lambdaQuery().eq(Food::getUserId, userId).
                        eq(Food::getId, id)
        );
        if (food == null) {
            return ResponseResult.errorResult(400, "该食物数据不存在");
        }
        return ResponseResult.okResult(food);
    }

    @Override
    public ResponseResult deleteFood(Long id) {
        if (id == null) {
            return ResponseResult.errorResult(400, "id不能为空");
        }
        Long userId = StpUtil.getLoginIdAsLong();
        Food food = this.getOne(
                Wrappers.<Food>lambdaQuery().eq(Food::getUserId, userId).
                        eq(Food::getId, id)
        );
        if (food == null) {
            return ResponseResult.errorResult(400, "该食物数据不存在");
        }
        this.removeById(id);
        return ResponseResult.okResult("删除成功");
    }

    @Override
    public ResponseResult updateFood(FoodDTO foodDTO) {
        if (foodDTO.getId() == null) {
            return ResponseResult.errorResult(400, "id不能为空");
        }
        Long userId = StpUtil.getLoginIdAsLong();
        Food food = this.getOne(
                Wrappers.<Food>lambdaQuery().eq(Food::getUserId, userId).
                        eq(Food::getId, foodDTO.getId())
        );
        if (food == null) {
            return ResponseResult.errorResult(400, "该食物数据不存在");
        }

//        food.setName(foodDTO.getName());
//        food.setEn(foodDTO.getEn());
//        food.setDensity(foodDTO.getDensity());
//        food.setCarbohydrateMassDensity(foodDTO.getCarbohydrateMassDensity());
//        food.setCarbohydrateVolumeDensity(foodDTO.getCarbohydrateVolumeDensity());
//        food.setFatMassDensity(foodDTO.getFatMassDensity());
//        food.setFatVolumeDensity(foodDTO.getFatVolumeDensity());
//        food.setProteinMassDensity(foodDTO.getProteinMassDensity());
//        food.setProteinVolumeDensity(foodDTO.getProteinVolumeDensity());
//        food.setCelluloseMassDensity(foodDTO.getCelluloseMassDensity()\);
//        food.setCelluloseVolumeDensity(foodDTO.getCelluloseVolumeDensity());
//        food.setCalorieMassDensity(foodDTO.getCalorieMassDensity());
//        food.setCalorieVolumeDensity(foodDTO.getCalorieVolumeDensity());
//        food.setCategory(foodDTO.getCategory());
//
        BeanUtils.copyProperties(foodDTO,food);

        this.updateById(food);
        return ResponseResult.okResult("修改成功");
    }

    @Override
    public ResponseResult addFood(FoodAddDTO foodAddDTO) {
        if (foodAddDTO.getName() == null) {
            return ResponseResult.errorResult(400, "名称不能为空");
        }
        Long userId = StpUtil.getLoginIdAsLong();
        Food food = new Food();
        BeanUtils.copyProperties(foodAddDTO,food);
        food.setId(null);
        food.setUserId(userId);
        this.save(food);
        return ResponseResult.okResult("添加成功");
    }

//    @Override
//    public ResponseResult getByName(String name) {
//        List<Food> foods = this.list(Wrappers.<Food>lambdaQuery().like(StrUtil.isNotBlank(name), Food::getName, name));
//
//        return ResponseResult.okResult(foods);
//    }
    @Override
    public Long getIdByName(String name) {
        Food food = this.getOne(Wrappers.<Food>lambdaQuery().eq(Food::getName, name));
        if (food == null) {
            return null;
        }
        return food.getId();
    }
}
