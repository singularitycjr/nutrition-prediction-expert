package com.tongji.service.service;


import com.tongji.model.dto.AugorithmReturnDTO;
import com.tongji.model.dto.FoodChosenDTO;
import com.tongji.model.dto.RecognizeDTO;
import com.tongji.model.json.FoodChoices;
import com.tongji.model.json.LapDepthJSON;

import java.util.List;

public interface IAlgorithmService {
    FoodChoices getPredictInfo(RecognizeDTO recognizeDTO);

    List<AugorithmReturnDTO> getNutritionInfo(FoodChosenDTO foodChosenDTO);
}
