package com.tongji.utils;

import com.alibaba.fastjson.JSON;
import com.tongji.pojo.Message;


public class MessageUtils {

    public static String getMessage(String toUserId, String fromUserId, String message) {
        Message result = new Message(toUserId, fromUserId, message);
        return JSON.toJSONString(result);
    }
}
