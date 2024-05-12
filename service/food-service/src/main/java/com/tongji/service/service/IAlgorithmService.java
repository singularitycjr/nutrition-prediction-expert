package com.tongji.service.service;


import com.tongji.model.dto.patient.AugorithmReturnDTO;
import com.tongji.model.dto.patient.FoodChosenDTO;
import com.tongji.model.dto.patient.RecognizeDTO;
import com.tongji.model.json.FoodChoices;

import java.util.List;

public interface IAlgorithmService {
    FoodChoices getPredictInfo(RecognizeDTO recognizeDTO);

    List<AugorithmReturnDTO> getNutritionInfo(FoodChosenDTO foodChosenDTO);


}
