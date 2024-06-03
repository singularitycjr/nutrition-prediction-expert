package com.tongji.websocket.manager;

import jakarta.websocket.Session;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class WebSocketSessionManager {
    private static final Map<String, Session> onlineUsers = new ConcurrentHashMap<>();

    public static Map<String, Session> getOnlineUsers() {
        return onlineUsers;
    }
}