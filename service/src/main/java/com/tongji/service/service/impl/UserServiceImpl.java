package com.tongji.service.service.impl;

import com.tongji.service.mapper.UserMapper;
import com.tongji.service.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.tongji.model.pojos.User;

/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-10-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
