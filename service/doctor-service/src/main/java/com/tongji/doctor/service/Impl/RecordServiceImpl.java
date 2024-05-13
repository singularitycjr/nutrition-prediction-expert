package com.tongji.doctor.service.Impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tongji.doctor.mapper.RecordDetailMapper;
import com.tongji.doctor.mapper.RecordMapper;
import com.tongji.doctor.mapper.UserMapper;
import com.tongji.doctor.service.IRecordService;
import com.tongji.global.util.SaTokenUtil;
import com.tongji.model.dto.doctor.TimeRangeDTO;
import com.tongji.model.pojo.Record;
import com.tongji.model.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {
    @Autowired
    RecordDetailMapper recordDetailMapper;

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
            List<RecordDetail> recordDetailList = recordDetailService.list(
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

                // 根据记录类型，更新对应餐次的营养物质总量
                if (Objects.equals(record.getType(), "早餐")) {
                    recordStatisticDTO.setBreakfastCalorieMass(recordStatisticDTO.getBreakfastCalorieMass().add(recordDetail.getCalorieMass() != null ? recordDetail.getCalorieMass() : BigDecimal.ZERO));
                } else if (Objects.equals(record.getType(), "午餐")) {
                    recordStatisticDTO.setLunchCalorieMass(recordStatisticDTO.getLunchCalorieMass().add(recordDetail.getCalorieMass() != null ? recordDetail.getCalorieMass() : BigDecimal.ZERO));
                } else if (Objects.equals(record.getType(), "晚餐")) {
                    recordStatisticDTO.setDinnerCalorieMass(recordStatisticDTO.getDinnerCalorieMass().add(recordDetail.getCalorieMass() != null ? recordDetail.getCalorieMass() : BigDecimal.ZERO));
                } else if (Objects.equals(record.getType(), "加餐")) {
                    recordStatisticDTO.setSnackCalorieMass(recordStatisticDTO.getSnackCalorieMass().add(recordDetail.getCalorieMass() != null ? recordDetail.getCalorieMass() : BigDecimal.ZERO));
                }
            }
        }

        // 将统计结果按日期排序
        List<Map.Entry<LocalDate, RecordStatisticDTO>> sortedStatistics = new ArrayList<>(dailyStatistics.entrySet());
        sortedStatistics.sort(Map.Entry.comparingByKey());

        // 构造最终返回结果
        List<RecordStatisticDTO> result = sortedStatistics.stream().map(Map.Entry::getValue).collect(Collectors.toList());

        return ResponseResult.okResult(result);
    }

}
