package com.tongji.ws;

import com.tongji.utils.MessageUtils;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/{userId}")
@Component
@Slf4j
public class MessageServer {
    private static final Map<String,Session> onlineUsers = new ConcurrentHashMap<>();

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
//        Message messageObj = JSON.parseObject(message, Message.class);
//        String toUserId = messageObj.getToUserId();
//        String msg = messageObj.getMessage();
//        sendMessageToUser(userId, toUserId, msg);
        sendToAllClient(message);
    }

    private void sendMessageToUser(String fromUserId, String toUserId, String message) throws IOException {
        Session session = onlineUsers.get(toUserId);
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
