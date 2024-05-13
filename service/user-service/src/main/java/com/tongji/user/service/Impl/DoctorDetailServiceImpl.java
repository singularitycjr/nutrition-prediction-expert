package com.tongji.user.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tongji.model.pojo.DoctorDetail;
import com.tongji.model.pojo.UserDetail;
import com.tongji.user.mapper.DoctorDetailMapper;
import com.tongji.user.mapper.UserDetailMapper;
import com.tongji.user.service.IDoctorDetailService;
import com.tongji.user.service.IUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 医生详细信息 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-10-18
 */
@Service
@Slf4j
public class DoctorDetailServiceImpl extends ServiceImpl<DoctorDetailMapper, DoctorDetail> implements IDoctorDetailService {

}
