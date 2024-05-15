package com.tongji.doctor.service.Impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tongji.doctor.mapper.FoodMapper;
import com.tongji.doctor.mapper.RecordDetailMapper;
import com.tongji.doctor.mapper.RecordMapper;
import com.tongji.doctor.service.IRecordService;
import com.tongji.global.util.SaTokenUtil;
import com.tongji.model.dto.common.RecordDetailReturnDTO;
import com.tongji.model.dto.doctor.RecordStatisticDTO;
import com.tongji.model.dto.doctor.TimeRangeDTO;
import com.tongji.model.dto.patient.RecordDTO;
import com.tongji.model.pojo.Food;
import com.tongji.model.pojo.Record;
import com.tongji.model.pojo.RecordDetail;
import com.tongji.model.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {
    @Autowired
    RecordDetailMapper recordDetailMapper;

    @Autowired
    FoodMapper foodMapper;

    @Override
    public ResponseResult statisticByDay(TimeRangeDTO timeRangeDTO) {
        if (timeRangeDTO.getStartTime() == null || timeRangeDTO.getEndTime() == null ||  timeRangeDTO.getId()==null) {
            return ResponseResult.errorResult(400, "时间范围及患者id不能为空");
        }

        Long id = timeRangeDTO.getId();
        // 查询在时间范围内的所有记录
        List<Record> recordList = this.list(
                Wrappers.<Record>lambdaQuery()
                        .eq(Record::getUserId, id)
                        .between(Record::getCreateTime, timeRangeDTO.getStartTime(), timeRangeDTO.getEndTime())
        );

        // 用于存储每天的统计结果，以日期为 key
        Map<LocalDate, RecordStatisticDTO> dailyStatistics = new HashMap<>();

        // 遍历每条记录
        for (Record record : recordList) {
            LocalDate recordDate = record.getCreateTime().toLocalDate(); // 获取记录日期

            // 如果该日期不存在于统计结果中，则新建一个统计对象
            dailyStatistics.putIfAbsent(recordDate, new RecordStatisticDTO());

            // 获取该日期的统计对象
            RecordStatisticDTO recordStatisticDTO = dailyStatistics.get(recordDate);

            // 获取该记录的所有明细
            List<RecordDetail> recordDetailList = recordDetailMapper.selectList(
                    Wrappers.<RecordDetail>lambdaQuery().eq(RecordDetail::getRecordId, record.getId())
            );

            // 遍历该记录的所有明细
            for (RecordDetail recordDetail : recordDetailList) {
                // 根据明细中的类型，更新对应的营养物质总量
                recordStatisticDTO.setCalorieMass(recordStatisticDTO.getCalorieMass().add(recordDetail.getCalorieMass() != null ? recordDetail.getCalorieMass() : BigDecimal.ZERO));
                recordStatisticDTO.setCarbohydrateMass(recordStatisticDTO.getCarbohydrateMass().add(recordDetail.getCarbohydrateMass() != null ? recordDetail.getCarbohydrateMass() : BigDecimal.ZERO));
                recordStatisticDTO.setFatMass(recordStatisticDTO.getFatMass().add(recordDetail.getFatMass() != null ? recordDetail.getFatMass() : BigDecimal.ZERO));
                recordStatisticDTO.setProteinMass(recordStatisticDTO.getProteinMass().add(recordDetail.getProteinMass() != null ? recordDetail.getProteinMass() : BigDecimal.ZERO));
                recordStatisticDTO.setCelluloseMass(recordStatisticDTO.getCelluloseMass().add(recordDetail.getCelluloseMass() != null ? recordDetail.getCelluloseMass() : BigDecimal.ZERO));

            }
        }

        // 将统计结果按日期排序
        List<Map.Entry<LocalDate, RecordStatisticDTO>> sortedStatistics = new ArrayList<>(dailyStatistics.entrySet());
        sortedStatistics.sort(Map.Entry.comparingByKey());

        return ResponseResult.okResult(sortedStatistics);
    }

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
    public ResponseResult getDetailByRecordId(Long id) {
        if (id== null ) {
            return ResponseResult.errorResult(400, "RecordId不能为空");
        }
        List<RecordDetail> recordDetailList = recordDetailMapper.selectList(
                Wrappers.<RecordDetail>lambdaQuery().
                        eq(RecordDetail::getRecordId, id)
        );
        List<RecordDetailReturnDTO> recordDetailReturnList=new ArrayList<>();
        for(RecordDetail recordDetail:recordDetailList){
            String foodName=foodMapper.selectOne(
                    Wrappers.<Food>lambdaQuery().eq(Food::getId, recordDetail.getFoodId())
            ).getName();

            RecordDetailReturnDTO returnDTO=new RecordDetailReturnDTO();
            returnDTO.setId(recordDetail.getId());
            returnDTO.setFoodId(recordDetail.getFoodId());
            returnDTO.setRecordId(id);

            returnDTO.setFoodName(foodName);
            returnDTO.setCalorieMass(recordDetail.getCalorieMass());
            returnDTO.setCarbohydrateMass(recordDetail.getCarbohydrateMass());
            returnDTO.setCelluloseMass(recordDetail.getCelluloseMass());
            returnDTO.setFatMass(recordDetail.getFatMass());
            returnDTO.setProteinMass(recordDetail.getProteinMass());
            returnDTO.setFoodMass(recordDetail.getFoodMass());

            recordDetailReturnList.add(returnDTO);
        }

        return ResponseResult.okResult(recordDetailReturnList);
    }

}
