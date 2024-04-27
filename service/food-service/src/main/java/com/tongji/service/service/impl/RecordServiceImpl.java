package com.tongji.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tongji.common.enums.AppHttpCodeEnum;
import com.tongji.global.util.SaTokenUtil;
import com.tongji.model.dto.*;
import com.tongji.model.json.FoodChoices;
import com.tongji.model.pojo.Record;
import com.tongji.model.pojo.RecordDetail;
import com.tongji.model.vo.GoBankNutritionVO;
import com.tongji.model.vo.GoBankSegRecVO;
import com.tongji.model.vo.ResponseResult;
import com.tongji.service.mapper.RecordMapper;
import com.tongji.service.service.IAlgorithmService;
import com.tongji.service.service.IFoodService;
import com.tongji.service.service.IRecordDetailService;
import com.tongji.service.service.IRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 预测信息 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-11-29
 */
@Service
@Slf4j
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {

    @Autowired
    private IAlgorithmService algorithmService;
    @Autowired
    private IFoodService foodService;
    @Autowired
    private IRecordDetailService recordDetailService;
    @Autowired
    private Environment environment;


    @Override
    public ResponseResult getRecord(TimeRangeDTO timeRangeDTO) {
        if (timeRangeDTO.getStartTime() == null || timeRangeDTO.getEndTime() == null) {
            return ResponseResult.errorResult(400, "时间范围不能为空");
        }
        Long id = SaTokenUtil.getId();
        List<Record> recordList = this.list(
                Wrappers.<Record>lambdaQuery().eq(Record::getUserId, id).
                        between(Record::getCreateTime, timeRangeDTO.getStartTime(), timeRangeDTO.getEndTime())
        );

        List<RecordDTO> recordDTOList =new ArrayList<RecordDTO>();
        for(Record record:recordList){
            RecordDTO recordDTO = new RecordDTO();
            BeanUtils.copyProperties(record,recordDTO);
            recordDTOList.add(recordDTO);
        }
        recordDTOList.sort(Comparator.comparing(RecordDTO::getCreateTime));
        return ResponseResult.okResult(recordDTOList);
    }

    @Override
    public ResponseResult deleteRecord(Long id) {
        //数据库已设置了record对record_detail的级联删除
        if (id == null) {
            return ResponseResult.errorResult(400, "id不能为空");
        }
        Long userId = SaTokenUtil.getId();
        Record record = this.getOne(
                Wrappers.<Record>lambdaQuery().eq(Record::getUserId, userId).
                        eq(Record::getId, id)
        );
        if (record == null) {
            return ResponseResult.errorResult(400, "该饮食数据不存在");
        }
        this.removeById(id);
        return ResponseResult.okResult("删除成功");
    }

    @Override
    public ResponseResult updateRecord(RecordDTO recordDTO){
        if (recordDTO.getId() == null||recordDTO.getCreateTime()==null||recordDTO.getType()==null) {
            return ResponseResult.errorResult(400, "id、时间、类型不能为空");
        }
        Long userId = SaTokenUtil.getId();
        Record record = this.getOne(
                Wrappers.<Record>lambdaQuery().eq(Record::getUserId, userId).
                        eq(Record::getId, recordDTO.getId())
        );
        if (record == null) {
            return ResponseResult.errorResult(400, "该饮食数据不存在");
        }
        BeanUtils.copyProperties(recordDTO,record);
        this.updateById(record);
        return ResponseResult.okResult("修改成功");

    }

    @Override
    public ResponseResult addRecord(RecordAddDTO recordAddDTO) {
        if (recordAddDTO.getCreateTime() == null || recordAddDTO.getType() == null) {
            return ResponseResult.errorResult(400, "时间和类型不能为空");
        }
        Long userId = SaTokenUtil.getId();
        Record record=new Record();

        BeanUtils.copyProperties(recordAddDTO,record);
        record.setUserId(userId);
        this.save(record);

        RecordAddReturnDTO recordAddReturnDTO=new RecordAddReturnDTO();
        recordAddReturnDTO.setId(record.getId());
        return ResponseResult.okResult(recordAddReturnDTO);
    }

    @Override
    public ResponseResult recognize(RecognizeDTO recognizeDTO) {
        if (recognizeDTO.getUrl() == null || recognizeDTO.getNum() == null) {
            return ResponseResult.errorResult(400, "url和num不能为空");
        }
        FoodChoices result = algorithmService.getPredictInfo(recognizeDTO);
        log.info("识别结果: {}", result);
        return ResponseResult.okResult(result);
    }

    @Override
    public ResponseResult nutrition(FoodChosenDTO foodChosenDTO) {
        List<AugorithmReturnDTO> nutritionList = algorithmService.getNutritionInfo(foodChosenDTO);
        // 遍历lapDepthJSON中的result
        for (AugorithmReturnDTO dto : nutritionList) {
            // 通过食物名字获取食物id
            Long id = this.foodService.getIdByName(dto.getName());
            dto.setId(id);
        }

        log.info("营养评估结果: {}", nutritionList);
        return ResponseResult.okResult(nutritionList);
    }

    @Override
    public ResponseResult statistic(TimeRangeDTO timeRangeDTO) {
        if (timeRangeDTO.getStartTime() == null || timeRangeDTO.getEndTime() == null) {
            return ResponseResult.errorResult(400, "时间范围不能为空");
        }
        Long id = SaTokenUtil.getId();
        List<Record> recordList = this.list(
                Wrappers.<Record>lambdaQuery().eq(Record::getUserId, id).
                        between(Record::getCreateTime, timeRangeDTO.getStartTime(), timeRangeDTO.getEndTime())
        );

        RecordStatisticDTO recordStatisticDTO = new RecordStatisticDTO();

        for (Record record : recordList) {
            List<RecordDetail> recordDetailList = recordDetailService.list(
                    Wrappers.<RecordDetail>lambdaQuery().eq(RecordDetail::getRecordId, record.getId())
            );
            for (RecordDetail recordDetail : recordDetailList) {
                recordStatisticDTO.setCalorieMass(recordStatisticDTO.getCalorieMass().add(recordDetail.getCalorieMass() != null ? recordDetail.getCalorieMass() : new BigDecimal(0)));
                recordStatisticDTO.setCarbohydrateMass(recordStatisticDTO.getCarbohydrateMass().add(recordDetail.getCarbohydrateMass() != null ? recordDetail.getCarbohydrateMass() : new BigDecimal(0)));
                recordStatisticDTO.setFatMass(recordStatisticDTO.getFatMass().add(recordDetail.getFatMass() != null ? recordDetail.getFatMass() : new BigDecimal(0)));
                recordStatisticDTO.setProteinMass(recordStatisticDTO.getProteinMass().add(recordDetail.getProteinMass() != null ? recordDetail.getProteinMass() : new BigDecimal(0)));
                recordStatisticDTO.setCelluloseMass(recordStatisticDTO.getCelluloseMass().add(recordDetail.getCelluloseMass() != null ? recordDetail.getCelluloseMass() : new BigDecimal(0)));

                if (Objects.equals(record.getType(), "早餐")) {
                    recordStatisticDTO.setBreakfastCalorieMass(recordStatisticDTO.getBreakfastCalorieMass().add(recordDetail.getCalorieMass() != null ? recordDetail.getCalorieMass() : new BigDecimal(0)));
                } else if (Objects.equals(record.getType(), "午餐")) {
                    recordStatisticDTO.setLunchCalorieMass(recordStatisticDTO.getLunchCalorieMass().add(recordDetail.getCalorieMass() != null ? recordDetail.getCalorieMass() : new BigDecimal(0)));
                } else if (Objects.equals(record.getType(), "晚餐")) {
                    recordStatisticDTO.setDinnerCalorieMass(recordStatisticDTO.getDinnerCalorieMass().add(recordDetail.getCalorieMass() != null ? recordDetail.getCalorieMass() : new BigDecimal(0)));
                } else if (Objects.equals(record.getType(), "加餐")) {
                    recordStatisticDTO.setSnackCalorieMass(recordStatisticDTO.getSnackCalorieMass().add(recordDetail.getCalorieMass() != null ? recordDetail.getCalorieMass() : new BigDecimal(0)));
                }
            }
        }
        return ResponseResult.okResult(recordStatisticDTO);
    }


    @Override
    public ResponseResult segrec(SegRecDTO segRecDTO) {
        Environment environment = new StandardEnvironment();
        RestTemplate restTemplate = new RestTemplate();
        String url= environment.getProperty("algorithmUrl.segRec");

        // 为restTemplate添加请求头
        /* 请求头 */
        HttpHeaders header = new HttpHeaders();

        header.add("x-api-key", environment.getProperty("x-api-key"));

        HttpEntity<SegRecDTO> httpEntity = new HttpEntity<>(segRecDTO, header);

        String res = restTemplate.postForEntity(url, httpEntity, String.class).getBody();
        GoBankSegRecVO goBankSegRecVO = JSON.parseObject(res, GoBankSegRecVO.class);
        if (goBankSegRecVO == null || goBankSegRecVO.getCode() == null || goBankSegRecVO.getCode() != 200) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }
        return ResponseResult.okResult(goBankSegRecVO);
    }
    @Override
    public ResponseResult nutrition(NutritionDTO nutritionDTO) {
        RestTemplate restTemplate = new RestTemplate();
        String url= environment.getProperty("algorithmUrl.nutrition");

        // 为restTemplate添加请求头
        /* 请求头 */
        HttpHeaders header = new HttpHeaders();

        header.add("x-api-key", environment.getProperty("x-api-key"));

        HttpEntity<NutritionDTO> httpEntity = new HttpEntity<>(nutritionDTO, header);

        String res = restTemplate.postForEntity(url, httpEntity, String.class).getBody();

        GoBankNutritionVO goBankSegRecVO = JSON.parseObject(res, GoBankNutritionVO.class);
        if (goBankSegRecVO == null || goBankSegRecVO.getCode() == null || goBankSegRecVO.getCode() != 200) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }
        return ResponseResult.okResult(goBankSegRecVO.format());
    }
}
