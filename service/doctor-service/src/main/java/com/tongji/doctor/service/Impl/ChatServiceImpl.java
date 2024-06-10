package com.tongji.doctor.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tongji.doctor.mapper.ChatReadMapper;
import com.tongji.doctor.mapper.GlucoseMapper;
import com.tongji.doctor.service.IChatReadService;
import com.tongji.model.pojo.ChatRead;
import com.tongji.model.pojo.Glucose;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatReadMapper, ChatRead>  implements IChatReadService {

}

