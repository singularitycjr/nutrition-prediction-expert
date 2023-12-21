package com.tongji.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.tongji.common.constants.CommonConstants;
import com.tongji.common.service.Impl.CacheService;
import com.tongji.common.utils.HttpClientUtil;
import com.tongji.model.dto.AugorithmReturnDTO;
import com.tongji.model.dto.FoodChosenDTO;
import com.tongji.model.dto.RecognizeDTO;
import com.tongji.model.json.FoodChoices;
import com.tongji.model.json.LapDepthJSON;
import com.tongji.service.properties.AlgorithmProperties;
import com.tongji.service.service.IAlgorithmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class AlgorithmServiceImpl implements IAlgorithmService {

    @Autowired
    private AlgorithmProperties algorithmProperties;

    @Autowired
    private CacheService cacheService;

    @Override
    public FoodChoices getPredictInfo(RecognizeDTO recognizeDTO) {
        if (!algorithmProperties.getEnableAlgorithm()) {
            FoodChoices foodChoices = new FoodChoices();
            FoodChoices.result result = new FoodChoices.result();
            result.setFood(38);
            result.setTop5(List.of("海参", "紫菜包饭", "玉米棒", "青椒", "皮蛋"));

            FoodChoices.result result1 = new FoodChoices.result();
            result1.setFood(135);
            result1.setTop5(List.of("荞麦馒头", "馒头", "芝麻糊", "菜包", "南瓜紫薯馒头"));

            FoodChoices.result result2 = new FoodChoices.result();
            result2.setFood(203);
            result2.setTop5(List.of("鸡蛋", "牛奶", "双皮奶", "豆浆", "汤圆"));

            foodChoices.setResults(List.of(result, result1, result2));
            return foodChoices;
        }
        String url = recognizeDTO.getUrl();
        Integer num = recognizeDTO.getNum();
        long userId = StpUtil.getLoginIdAsLong();
        String key = CommonConstants.USER_IMG + userId;
        if (cacheService.get(key) != null) {
            throw new RuntimeException("请勿重复识别");
        }
        this.cacheService.setEx(key, url, CommonConstants.USER_IMG_TIMEOUT,
                CommonConstants.USER_IMG_TIMEOUT_TYPE);
        // 从url中获取到最后一段作为图片名
        String[] split = url.split("/");
        String imgName = split[split.length - 1];
        // 去掉后缀
        imgName = imgName.substring(0, imgName.lastIndexOf("."));
        log.info("imgName: {}", imgName);
        this.cacheService.setEx(CommonConstants.FOOD_IMG + imgName,
                String.valueOf(userId),
                CommonConstants.USER_IMG_TIMEOUT,
                CommonConstants.USER_IMG_TIMEOUT_TYPE);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", url);
        jsonObject.put("num", num);

        try {
            StringBuilder path = new StringBuilder()
                    .append(algorithmProperties.getHost());
            if (algorithmProperties.getCreateMaskPort() != null) {
                path.append(":")
                    .append(algorithmProperties.getCreateMaskPort());
            }
            if (algorithmProperties.getCreateMaskPath() != null) {
                path.append(algorithmProperties.getCreateMaskPath());
            }
            log.info("path: {}", path);
            String json = HttpClientUtil.doPostJson(path.toString(), jsonObject);
            FoodChoices foodChoices = JSON.parseObject(json, FoodChoices.class);
            log.info("foodChoices: {}", foodChoices);
            return foodChoices;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AugorithmReturnDTO> getNutritionInfo(FoodChosenDTO foodChosenDTO) {

        if (!algorithmProperties.getEnableAlgorithm()) {
            List<AugorithmReturnDTO> augorithmReturnDTOList = new ArrayList<>();

            AugorithmReturnDTO augorithmReturnDTO1 = new AugorithmReturnDTO();
//            augorithmReturnDTO1.setId(38L);
            augorithmReturnDTO1.setName("海参");
            augorithmReturnDTO1.setFoodMass(new BigDecimal("20.812"));
            augorithmReturnDTO1.setCarbohydrateMass(new BigDecimal("0.520"));
            augorithmReturnDTO1.setFatMass(new BigDecimal("0.042"));
            augorithmReturnDTO1.setProteinMass(new BigDecimal("3.434"));
            augorithmReturnDTO1.setCelluloseMass(new BigDecimal("0.000"));
            augorithmReturnDTO1.setCalorieMass(new BigDecimal("16.234"));
            augorithmReturnDTOList.add(augorithmReturnDTO1);

            AugorithmReturnDTO augorithmReturnDTO2 = new AugorithmReturnDTO();
//            augorithmReturnDTO2.setId(135L);
            augorithmReturnDTO2.setName("荞麦馒头");
            augorithmReturnDTO2.setFoodMass(new BigDecimal("133.440"));
            augorithmReturnDTO2.setCarbohydrateMass(new BigDecimal("60.895"));
            augorithmReturnDTO2.setFatMass(new BigDecimal("2.311"));
            augorithmReturnDTO2.setProteinMass(new BigDecimal("12.142"));
            augorithmReturnDTO2.setCelluloseMass(new BigDecimal("2.712"));
            augorithmReturnDTO2.setCalorieMass(new BigDecimal("304.754"));
            augorithmReturnDTOList.add(augorithmReturnDTO2);


            AugorithmReturnDTO augorithmReturnDTO3 = new AugorithmReturnDTO();
            augorithmReturnDTO3.setName("鸡蛋");
            augorithmReturnDTO3.setFoodMass(new BigDecimal("33.920"));
            augorithmReturnDTO3.setCarbohydrateMass(new BigDecimal("0.814"));
            augorithmReturnDTO3.setFatMass(new BigDecimal("2.917"));
            augorithmReturnDTO3.setProteinMass(new BigDecimal("4.444"));
            augorithmReturnDTO3.setCelluloseMass(new BigDecimal("0.000"));
            augorithmReturnDTO3.setCalorieMass(new BigDecimal("471.492"));
            augorithmReturnDTOList.add(augorithmReturnDTO3);

            return augorithmReturnDTOList;
        }
        // 找到对应的图片
        long userId = StpUtil.getLoginIdAsLong();
        String url = this.cacheService.get(CommonConstants.USER_IMG + userId);
        if (url == null) {
            throw new RuntimeException("请先识别图片");
        }
        String maskUrl = this.cacheService.get(CommonConstants.USER_MASK_IMG + userId);
        if (maskUrl == null) {
            throw new RuntimeException("请先上传Mask图片");
        }
        // 删除缓存
        this.cacheService.delete(CommonConstants.USER_IMG + userId);
        this.cacheService.delete(CommonConstants.USER_MASK_IMG + userId);

        List<JSONObject> foodsJSON = new ArrayList<>();
        List<FoodChosenDTO.food> foodList = foodChosenDTO.getFoods();
        for (FoodChosenDTO.food food : foodList) {
            JSONObject foodJSON = new JSONObject();
            foodJSON.put("num", food.getNum());
            foodJSON.put("name", food.getName());
            foodsJSON.add(foodJSON);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("foods", foodsJSON);

        jsonObject.put("img_url", url);
        jsonObject.put("mask_url", maskUrl);


        try {
            List<AugorithmReturnDTO> augorithmReturnDTOList = new ArrayList<>();

            StringBuilder path = new StringBuilder()
                    .append(algorithmProperties.getHost());

            if (algorithmProperties.getUseMaskPort() != null) {
                path.append(":")
                        .append(algorithmProperties.getUseMaskPort());
            }
            if (algorithmProperties.getUseMaskPath() != null) {
                path.append(algorithmProperties.getUseMaskPath());
            }
            log.info("path: {}", path);
                    //.append(":")
                    //.append(algorithmProperties.getUseMaskPort())
                    //.append("/predict");
            String json = HttpClientUtil.doPostJson(path.toString(), jsonObject);
            LapDepthJSON lapDepthJSON = JSON.parseObject(json, LapDepthJSON.class);


            for(LapDepthJSON.Result result : lapDepthJSON.getResults()) {
                AugorithmReturnDTO augorithmReturnDTO = new AugorithmReturnDTO();
                augorithmReturnDTO.setName(result.getName());
                augorithmReturnDTO.setId(result.getId());
                for(LapDepthJSON.Nutrition nutrition : result.getNutrition()) {
                    if(nutrition.getKey().equals("质量"))
                        augorithmReturnDTO.setFoodMass(new BigDecimal(nutrition.getValue()));

                    if(nutrition.getKey().equals("碳水化合物"))
                        augorithmReturnDTO.setCarbohydrateMass(new BigDecimal(nutrition.getValue()));

                    if(nutrition.getKey().equals("脂肪"))
                        augorithmReturnDTO.setFatMass(new BigDecimal(nutrition.getValue()));

                    if(nutrition.getKey().equals("蛋白质"))
                        augorithmReturnDTO.setProteinMass(new BigDecimal(nutrition.getValue()));

                    if(nutrition.getKey().equals("纤维素"))
                        augorithmReturnDTO.setCelluloseMass(new BigDecimal(nutrition.getValue()));

                    if(nutrition.getKey().equals("热量"))
                        augorithmReturnDTO.setCalorieMass(new BigDecimal(nutrition.getValue()));
                }
                augorithmReturnDTOList.add(augorithmReturnDTO);
            }
//            log.info("lapDepthJSON: {}", lapDepthJSON);
            return augorithmReturnDTOList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
