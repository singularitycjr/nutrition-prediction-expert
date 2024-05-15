package com.tongji.doctor.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tongji.doctor.mapper.RecordDetailMapper;
import com.tongji.doctor.service.IRecordDetailService;
import com.tongji.model.pojo.RecordDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RecordDetailServiceImpl extends ServiceImpl<RecordDetailMapper, RecordDetail> implements IRecordDetailService {

}
