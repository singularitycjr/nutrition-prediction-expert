package com.tongji.websocket.ws;

import com.alibaba.fastjson.JSON;
import com.tongji.common.service.Impl.CacheService;
import com.tongji.websocket.pojo.Message;
import com.tongji.websocket.utils.MessageUtils;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/{userId}")
@Service
@Slf4j
public class MessageServer implements ApplicationContextAware {
    // 全局静态变量，保存 ApplicationContext
    private static ApplicationContext applicationContext;

    private CacheService cacheService;

    private static final Map<String, Session> onlineUsers = new ConcurrentHashMap<>();

    // 保存 Spring 注入的 ApplicationContext 到静态变量
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        MessageServer.applicationContext = applicationContext;
    }


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        onlineUsers.put(userId, session);
        System.out.println("New connection: " + session.getId() + " userId: " + userId);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        onlineUsers.remove(userId);
        System.out.println("Connection closed:  userId: " + userId);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @ Param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("userId") String userId) throws Exception {
        System.out.println("Message from " + userId + ": " + message);
        Message messageObj = JSON.parseObject(message, Message.class);
        String toUserId = messageObj.getToUserId();
        String msg = messageObj.getMessage();
        sendMessageToUser(userId, toUserId, msg);
//        sendToAllClient(message);
    }

    private void sendMessageToUser(String fromUserId, String toUserId, String message) throws IOException {

        Session session = onlineUsers.get(toUserId);
        if (session == null) {
            // 连接创建的时候，从 ApplicationContext 获取到 Bean 进行初始化
            this.cacheService = MessageServer.applicationContext.getBean(CacheService.class);
            if (!cacheService.exists(toUserId)) {
                List<String> messageList = new ArrayList<>();
                messageList.add(message);
                cacheService.set(toUserId, JSON.toJSONString(messageList));
            } else {
                String messageString = cacheService.get(toUserId);
                List<String> messageList = JSON.parseArray(messageString, String.class);
                messageList.add(message);
                cacheService.set(toUserId, JSON.toJSONString(messageList));

            }
        } else
            session.getBasicRemote().sendText(MessageUtils.getMessage(toUserId, fromUserId, message));


    }

    public void sendToAllClient(String message) {
        Collection<Session> sessions = onlineUsers.values();
        for (Session session : sessions) {
            try {
                //服务器向客户端发送消息
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }
}
