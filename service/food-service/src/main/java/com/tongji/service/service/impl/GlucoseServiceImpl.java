package com.tongji.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tongji.model.dto.GlucoseDTO;
import com.tongji.model.dto.TimeRangeDTO;
import com.tongji.model.pojo.Glucose;
import com.tongji.model.vo.ResponseResult;
import com.tongji.service.mapper.GlucoseMapper;
import com.tongji.service.service.IGlucoseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2023-11-29
 */
@Service
public class GlucoseServiceImpl extends ServiceImpl<GlucoseMapper, Glucose> implements IGlucoseService {


    @Override
    public ResponseResult getGlucose(TimeRangeDTO timeRangeDTO) {
        if (timeRangeDTO.getStartTime() == null || timeRangeDTO.getEndTime() == null) {
            return ResponseResult.errorResult(400, "时间范围不能为空");
        }
        Long id = StpUtil.getLoginIdAsLong();
        List<Glucose> glucoseList = this.list(
                Wrappers.<Glucose>lambdaQuery().eq(Glucose::getUserId, id).
                between(Glucose::getTime, timeRangeDTO.getStartTime(), timeRangeDTO.getEndTime())
        );
        return ResponseResult.okResult(glucoseList);
    }

    @Override
    public ResponseResult deleteGlucose(Long id) {
        if (id == null) {
            return ResponseResult.errorResult(400, "id不能为空");
        }
        Long userId = StpUtil.getLoginIdAsLong();
        Glucose glucose = this.getOne(
                Wrappers.<Glucose>lambdaQuery().eq(Glucose::getUserId, userId).
                eq(Glucose::getId, id)
        );
        if (glucose == null) {
            return ResponseResult.errorResult(400, "该血糖数据不存在");
        }
        this.removeById(id);
        return ResponseResult.okResult("删除成功");
    }

    @Override
    public ResponseResult updateGlucose(GlucoseDTO glucoseDTO) {
        if (glucoseDTO.getId() == null) {
            return ResponseResult.errorResult(400, "id不能为空");
        }
        Long userId = StpUtil.getLoginIdAsLong();
        Glucose glucose = this.getOne(
                Wrappers.<Glucose>lambdaQuery().eq(Glucose::getUserId, userId).
                eq(Glucose::getId, glucoseDTO.getId())
        );
        if (glucose == null) {
            return ResponseResult.errorResult(400, "该血糖数据不存在");
        }
        glucose.setGluValue(glucoseDTO.getGluValue());
        glucose.setTime(glucoseDTO.getTime());
        this.updateById(glucose);
        return ResponseResult.okResult("修改成功");
    }

    @Override
    public ResponseResult addGlucose(GlucoseDTO glucoseDTO) {
        if (glucoseDTO.getGluValue() == null || glucoseDTO.getTime() == null) {
            return ResponseResult.errorResult(400, "血糖值和时间不能为空");
        }
        Long userId = StpUtil.getLoginIdAsLong();
        Glucose glucose = new Glucose();
        glucose.setId(null);
        glucose.setUserId(userId);
        glucose.setGluValue(glucoseDTO.getGluValue());
        glucose.setTime(glucoseDTO.getTime());
        this.save(glucose);
        return ResponseResult.okResult("添加成功");
    }
}
