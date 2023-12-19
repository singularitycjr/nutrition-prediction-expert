package com.tongji.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.tongji.common.constants.CommonConstants;
import com.tongji.common.service.Impl.CacheService;
import com.tongji.common.utils.HttpClientUtil;
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
            result.setFood(1);
            result.setTop5(List.of("海参", "荞麦馒头", "鸡蛋", "鸡蛋", "鸡蛋"));

            FoodChoices.result result1 = new FoodChoices.result();
            result1.setFood(2);
            result1.setTop5(List.of("清蒸肉丸", "清蒸肉丸", "清蒸肉丸", "清蒸肉丸", "清蒸肉丸"));

            FoodChoices.result result2 = new FoodChoices.result();
            result2.setFood(3);
            result2.setTop5(List.of("水煮肉", "白菜", "清蒸肉丸", "清蒸肉丸", "清蒸肉丸"));

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
    public LapDepthJSON getNutritionInfo(FoodChosenDTO foodChosenDTO) {

        if (!algorithmProperties.getEnableAlgorithm()) {
            LapDepthJSON.Nutrition nutrition = new LapDepthJSON.Nutrition();
            nutrition.setKey("热量");
            nutrition.setValue("100");

            LapDepthJSON.Result result = new LapDepthJSON.Result();
            result.setName("海参");
            result.setNutrition(List.of(nutrition));

            LapDepthJSON.Result result1 = new LapDepthJSON.Result();
            result1.setName("清蒸肉丸");
            result1.setNutrition(List.of(nutrition));

            LapDepthJSON lapDepthJSON = new LapDepthJSON();
            lapDepthJSON.setResults(List.of(result, result1));

            return lapDepthJSON;
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
            log.info("lapDepthJSON: {}", lapDepthJSON);
            return lapDepthJSON;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
