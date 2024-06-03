package com.tongji.websocket.utils;

import com.alibaba.fastjson.JSON;
import com.tongji.model.dto.websocket.ChatSendDTO;
import com.tongji.model.dto.websocket.MessageSendDTO;
import com.tongji.model.pojo.Chat;
import com.tongji.model.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;


public class MessageUtils {

    public static String getMessage(Message messageObj) {
        MessageSendDTO messageSendDTO=new MessageSendDTO();
        messageSendDTO.setId(messageObj.getId());
        messageSendDTO.setMessage(messageObj.getMessage());
        messageSendDTO.setFromUserId(messageObj.getFromUserId());
        messageSendDTO.setFromUserRole(messageObj.getFromUserRole());
        messageSendDTO.setTime(messageObj.getTime());
        return JSON.toJSONString(messageSendDTO);
    }

    public static String getChat(Chat chatObj) {
        ChatSendDTO chatSendDTO=new ChatSendDTO();
        chatSendDTO.setMessage(chatObj.getMessage());
        chatSendDTO.setFromUserId(chatObj.getFromUserId());
        chatSendDTO.setFromUserRole(chatObj.getFromUserRole());
        chatSendDTO.setTime(chatObj.getTime());
        return JSON.toJSONString(chatSendDTO);
    }
}
