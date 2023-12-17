package com.tongji.service.service;


import com.tongji.model.dto.FoodChosenDTO;
import com.tongji.model.dto.RecognizeDTO;
import com.tongji.model.json.FoodChoices;
import com.tongji.model.json.LapDepthJSON;

public interface IAlgorithmService {
    FoodChoices getPredictInfo(RecognizeDTO recognizeDTO);

    LapDepthJSON getNutritionInfo(FoodChosenDTO foodChosenDTO);
}
