package com.tongji.websocket.ws;

import com.alibaba.fastjson.JSON;
import com.tongji.common.service.Impl.CacheService;
import com.tongji.global.constrants.Constrants;
import com.tongji.global.enums.MessageTypeEnum;
import com.tongji.global.enums.RoleEnum;
import com.tongji.global.util.ConnectionUtil;
import com.tongji.global.util.SaTokenUtil;
import com.tongji.model.dto.websocket.CommonMessageDTO;
import com.tongji.model.pojo.Chat;
import com.tongji.model.pojo.Message;
import com.tongji.messagechat.mapper.DoctorMapper;
import com.tongji.messagechat.mapper.UserMapper;
import com.tongji.messagechat.service.Impl.ChatServiceImpl;
import com.tongji.messagechat.service.Impl.MessageServiceImpl;
import com.tongji.websocket.manager.WebSocketSessionManager;
import com.tongji.websocket.utils.MessageUtils;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value="/ws/message/{satoken}")
@Component
@Slf4j
public class MessageServer implements ApplicationContextAware {
    // 全局静态变量，保存 ApplicationContext
    private static ApplicationContext applicationContext;

    //存储目前与服务器建立了连接的会话
    private static final Map<String,Session> onlineUsers = new ConcurrentHashMap<>();

    private CacheService cacheService;
    private UserMapper userMapper;
    private DoctorMapper doctorMapper;
    private ChatServiceImpl chatService;
    private MessageServiceImpl messageService;



    // 保存 Spring 注入的 ApplicationContext 到静态变量
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        MessageServer.applicationContext = applicationContext;
    }


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("satoken") String satoken) {
        String userId=SaTokenUtil.getRoleByToken(satoken)+SaTokenUtil.getIdByToken(satoken);
        onlineUsers.put(userId, session);//将该会话添加到map中
        System.out.println("New connection(message): " + session.getId() + " userId: " + userId);

        // 连接创建的时候，从 ApplicationContext 获取到 Bean 进行初始化
        this.cacheService = MessageServer.applicationContext.getBean(CacheService.class);
        this.userMapper= MessageServer.applicationContext.getBean(UserMapper.class);
        this.doctorMapper= MessageServer.applicationContext.getBean(DoctorMapper.class);
        this.chatService= MessageServer.applicationContext.getBean(ChatServiceImpl.class);
        this.messageService= MessageServer.applicationContext.getBean(MessageServiceImpl.class);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("satoken") String satoken) {
        String userId=SaTokenUtil.getRoleByToken(satoken)+SaTokenUtil.getIdByToken(satoken);
        onlineUsers.remove(userId);
        System.out.println("Connection closed(message):  userId: " + userId);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @ Param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("satoken") String satoken) throws Exception {
        String userId=SaTokenUtil.getRoleByToken(satoken)+SaTokenUtil.getIdByToken(satoken);
        System.out.println("Message from " + userId + ": " + message);
        CommonMessageDTO commonMessageDTO = JSON.parseObject(message, CommonMessageDTO.class);
        String toUserRole= "";

        if(Objects.equals(SaTokenUtil.getRoleByToken(satoken), RoleEnum.PATIENT.getName()))
            toUserRole=RoleEnum.DOCTOR.getName();
        else
            toUserRole=RoleEnum.PATIENT.getName();


            Message messageObj=new Message();
            messageObj.setMessage(commonMessageDTO.getMessage());
            messageObj.setConfirmed(Boolean.FALSE);
            messageObj.setTime(commonMessageDTO.getTime());
            messageObj.setFromUserId(SaTokenUtil.getIdByToken(satoken));
            messageObj.setFromUserRole(SaTokenUtil.getRoleByToken(satoken));
            messageObj.setToUserId(commonMessageDTO.getToUserId());
            messageObj.setToUserRole(toUserRole);
            sendMessageToUser(messageObj);


        System.out.println("Message sent(message): userId: " + userId);
//        sendToAllClient(message);
    }

    private void sendMessageToUser(Message messageObj) throws IOException {
        String toUserId=messageObj.getToUserRole()+messageObj.getToUserId();
        String fromUserId=messageObj.getFromUserRole()+messageObj.getFromUserId();
        messageService.save(messageObj);

        Session session = onlineUsers.get(toUserId);
        if(session!=null)
        session.getBasicRemote().sendText(MessageUtils.getMessage(messageObj));


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
