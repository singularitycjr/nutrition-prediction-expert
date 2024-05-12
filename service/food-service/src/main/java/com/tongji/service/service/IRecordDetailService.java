package com.tongji.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.dto.patient.RecordDetailAddDTO;
import com.tongji.model.dto.patient.RecordDetailDTO;
import com.tongji.model.pojo.RecordDetail;
import com.tongji.model.vo.ResponseResult;

/**
 * <p>
 * 预测信息细节 服务类
 * </p>
 *
 * @author author
 * @since 2023-11-29
 */
public interface IRecordDetailService extends IService<RecordDetail> {
    ResponseResult getById(Long id);

    ResponseResult getByRecordId(Long record_id);

    ResponseResult deleteRecordDetail(Long id);

    ResponseResult updateRecordDetail(RecordDetailDTO recordDetailDTO);

    ResponseResult addRecordDetail(RecordDetailAddDTO recordDetailAddDTO);

}
