package com.tongji.websocket.ws;

import com.alibaba.fastjson.JSON;
import com.tongji.common.service.Impl.CacheService;
import com.tongji.global.constrants.Constrants;
import com.tongji.global.enums.RoleEnum;
import com.tongji.global.util.ConnectionUtil;
import com.tongji.global.util.SaTokenUtil;
import com.tongji.messagechat.mapper.DoctorMapper;
import com.tongji.messagechat.mapper.UserMapper;
import com.tongji.messagechat.service.Impl.ChatServiceImpl;
import com.tongji.messagechat.service.Impl.MessageServiceImpl;
import com.tongji.model.dto.websocket.CommonMessageDTO;
import com.tongji.model.pojo.Chat;
import com.tongji.websocket.manager.WebSocketSessionManager;
import com.tongji.websocket.utils.MessageUtils;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value="/ws/chat/{satoken}")
@Component
@Slf4j
public class ChatServer implements ApplicationContextAware {
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
        ChatServer.applicationContext = applicationContext;
    }


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("satoken") String satoken) {
        String userId=SaTokenUtil.getRoleByToken(satoken)+SaTokenUtil.getIdByToken(satoken);
        onlineUsers.put(userId, session);//将该会话添加到map中
        System.out.println("New connection(chat): " + session.getId() + " userId: " + userId);

        // 连接创建的时候，从 ApplicationContext 获取到 Bean 进行初始化
        this.cacheService = ChatServer.applicationContext.getBean(CacheService.class);
        this.userMapper= ChatServer.applicationContext.getBean(UserMapper.class);
        this.doctorMapper= ChatServer.applicationContext.getBean(DoctorMapper.class);
        this.chatService= ChatServer.applicationContext.getBean(ChatServiceImpl.class);
        this.messageService= ChatServer.applicationContext.getBean(MessageServiceImpl.class);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("satoken") String satoken) {
        String userId=SaTokenUtil.getRoleByToken(satoken)+SaTokenUtil.getIdByToken(satoken);
        onlineUsers.remove(userId);
        System.out.println("Connection closed(chat):  userId: " + userId);
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


            Chat chat=new Chat();
            chat.setMessage(commonMessageDTO.getMessage());
            chat.setTime(commonMessageDTO.getTime());
            chat.setFromUserId(SaTokenUtil.getIdByToken(satoken));
            chat.setFromUserRole(SaTokenUtil.getRoleByToken(satoken));
            chat.setToUserId(commonMessageDTO.getToUserId());
            chat.setToUserRole(toUserRole);
            sendChatToUser(chat);

        System.out.println("Message sent(chat): userId: " + userId);
//        sendToAllClient(message);
    }

    private void sendChatToUser(Chat chat) throws IOException {
        String toUserId=chat.getToUserRole()+chat.getToUserId();
        String fromUserId=chat.getFromUserRole()+chat.getFromUserId();

        String chatId=ConnectionUtil.getChatId(fromUserId,toUserId);
        List<Chat> chatList;
        if (!cacheService.exists(chatId)) {
            chatList = new ArrayList<>();
            chatList.add(chat);

        } else {
            String chatStringList = cacheService.get(chatId);
             chatList = JSON.parseArray(chatStringList, Chat.class);
            chatList.add(chat);
        }

        if(chatList.size()>= Constrants.CHAT_SAVE_BATCH_SIZE) {
            chatService.saveBatch(chatList);
            chatList.clear();
        }
        cacheService.set(chatId, JSON.toJSONString(chatList));

        Session session = onlineUsers.get(toUserId);
        if(session!=null)
        session.getBasicRemote().sendText(MessageUtils.getChat(chat));
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
