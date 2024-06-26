package com.tongji.global.util;

import cn.dev33.satoken.stp.StpUtil;
import com.tongji.global.helper.LoginObj;
import com.alibaba.fastjson.JSON;


public  class SaTokenUtil {


    public static Long getId()
    {
        LoginObj loginObj = JSON.parseObject((String) StpUtil.getLoginId(), LoginObj.class);
        return loginObj.getId();
    }

    public static String getRole()
    {
        LoginObj loginObj = JSON.parseObject((String) StpUtil.getLoginId(), LoginObj.class);
        return loginObj.getRole();
    }

    public  static Long getIdByToken(String token)
    {
        LoginObj loginObj = JSON.parseObject((String) StpUtil.getLoginIdByToken(token), LoginObj.class);
        return loginObj.getId();
    }
    public static String getRoleByToken(String token)
    {
        LoginObj loginObj = JSON.parseObject((String) StpUtil.getLoginIdByToken(token), LoginObj.class);
        return loginObj.getRole();
    }
}
