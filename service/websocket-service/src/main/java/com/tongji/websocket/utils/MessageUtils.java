package com.tongji.websocket.utils;

import com.alibaba.fastjson.JSON;
import com.tongji.websocket.pojo.Message;


public class MessageUtils {

    public static String getMessage(String toUserId, String fromUserId, String message) {
        Message result = new Message(toUserId, fromUserId, message);
        return JSON.toJSONString(result);
    }
}
