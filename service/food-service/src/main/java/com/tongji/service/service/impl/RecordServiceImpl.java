package com.tongji.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tongji.model.dto.*;
import com.tongji.model.json.FoodChoices;
import com.tongji.model.json.LapDepthJSON;
import com.tongji.model.pojo.Record;
import com.tongji.model.vo.ResponseResult;
import com.tongji.service.mapper.RecordMapper;
import com.tongji.service.service.IAlgorithmService;
import com.tongji.service.service.IFoodService;
import com.tongji.service.service.IRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


    @Override
    public ResponseResult getRecord(TimeRangeDTO timeRangeDTO) {
        if (timeRangeDTO.getStartTime() == null || timeRangeDTO.getEndTime() == null) {
            return ResponseResult.errorResult(400, "时间范围不能为空");
        }
        Long id = StpUtil.getLoginIdAsLong();
        List<Record> recordList = this.list(
                Wrappers.<Record>lambdaQuery().eq(Record::getUserId, id).
                        between(Record::getCreateTime, timeRangeDTO.getStartTime(), timeRangeDTO.getEndTime())
        );
        return ResponseResult.okResult(recordList);
    }

    @Override
    public ResponseResult deleteRecord(Long id) {
        //数据库已设置了record对record_detail的级联删除
        if (id == null) {
            return ResponseResult.errorResult(400, "id不能为空");
        }
        Long userId = StpUtil.getLoginIdAsLong();
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
        Long userId = StpUtil.getLoginIdAsLong();
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
        Long userId = StpUtil.getLoginIdAsLong();
        Record record=new Record();

        BeanUtils.copyProperties(recordAddDTO,record);
        record.setUserId(userId);
        this.save(record);
        return ResponseResult.okResult("添加成功");
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
        LapDepthJSON lapDepthJSON = algorithmService.getNutritionInfo(foodChosenDTO);
        // 遍历lapDepthJSON中的result
        for (LapDepthJSON.Result result : lapDepthJSON.getResults()) {
            // 通过食物名字获取食物id
            Long id = this.foodService.getIdByName(result.getName());
            result.setId(id);
        }
        // Long id = this.foodService.getIdByName()
        log.info("营养评估结果: {}", lapDepthJSON);
        return ResponseResult.okResult(lapDepthJSON);
    }
}
