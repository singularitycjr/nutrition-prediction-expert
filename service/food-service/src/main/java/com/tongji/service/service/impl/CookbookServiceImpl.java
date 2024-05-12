package com.tongji.service.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tongji.model.dto.patient.CookBookReturnDTO;
import com.tongji.model.dto.patient.CookbookDTO;
import com.tongji.model.dto.patient.OtherFoodDTO;
import com.tongji.model.pojo.Cookbook;
import com.tongji.model.pojo.CookbookDetail;
import com.tongji.model.pojo.OtherFood;
import com.tongji.model.vo.ResponseResult;
import com.tongji.service.mapper.CookbookMapper;
import com.tongji.service.service.ICookbookDetailService;
import com.tongji.service.service.ICookbookService;
import com.tongji.service.service.IOtherFoodService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2023-12-18
 */
@Service
public class CookbookServiceImpl extends ServiceImpl<CookbookMapper, Cookbook> implements ICookbookService {

    @Autowired
    private ICookbookDetailService cookbookDetailService;

    @Autowired
    private IOtherFoodService otherFoodService;

    @Override
    public ResponseResult getCookbook(CookbookDTO cookbookDTO) {
        if(cookbookDTO.getSeason()==null||cookbookDTO.getLocation()==null||cookbookDTO.getNumber()==null){
            return ResponseResult.errorResult(400,"参数不完整");
        }

        Cookbook cookbook=this.getOne(
                Wrappers.<Cookbook>lambdaQuery().
                        eq(Cookbook::getSeason, cookbookDTO.getSeason()).
                        eq(Cookbook::getLocation, cookbookDTO.getLocation())
        );
        if(cookbook==null){
            return ResponseResult.errorResult(400,"该菜谱不存在");
        }

        CookbookDetail cookbookDetail=cookbookDetailService.getOne(
                Wrappers.<CookbookDetail>lambdaQuery().
                        eq(CookbookDetail::getCookbookId, cookbook.getId()).
                        eq(CookbookDetail::getNumber, cookbookDTO.getNumber())
        );
        if(cookbookDetail==null){
            return ResponseResult.errorResult(400,"该菜谱不存在");
        }
        List<OtherFood> otherFoodList=otherFoodService.list(
                Wrappers.<OtherFood>lambdaQuery().
                        eq(OtherFood::getDetailId, cookbookDetail.getId())
        );
        if(otherFoodList==null){
            return ResponseResult.errorResult(400,"该菜谱不存在");
        }

        CookBookReturnDTO cookBookReturnDTO =new CookBookReturnDTO();
        BeanUtils.copyProperties(cookbook, cookBookReturnDTO);
        cookBookReturnDTO.setOilSalt(cookbookDetail.getOilSalt());
        for(OtherFood otherFood:otherFoodList){
            OtherFoodDTO otherFoodDTO=new OtherFoodDTO();
            otherFoodDTO.setType(otherFood.getType());
            otherFoodDTO.setNote(otherFood.getName()+otherFood.getNote());
            cookBookReturnDTO.getFoods().add(otherFoodDTO);
        }

        return ResponseResult.okResult(cookBookReturnDTO);

    }
}
