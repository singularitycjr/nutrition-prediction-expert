package com.tongji.messagechat.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tongji.common.service.Impl.CacheService;
import com.tongji.global.constrants.Constrants;
import com.tongji.global.enums.RoleEnum;
import com.tongji.global.util.ConnectionUtil;
import com.tongji.global.util.SaTokenUtil;
import com.tongji.model.dto.websocket.HistoryMessageDTO;
import com.tongji.model.pojo.Chat;
import com.tongji.model.vo.ResponseResult;
import com.tongji.messagechat.mapper.ChatMapper;
import com.tongji.messagechat.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements IChatService {
    @Autowired
    CacheService cacheService;


    @Override
    public ResponseResult getLatest(Long otherSideId) {
        if (otherSideId == null)
            return ResponseResult.errorResult(400, "会话对方id不可为空");

        String chatId;
        Long patientId, doctorId;
        if (Objects.equals(SaTokenUtil.getRole(), RoleEnum.PATIENT.getName())) {
            patientId = SaTokenUtil.getId();
            doctorId = otherSideId;
        } else {
            patientId = otherSideId;
            doctorId = SaTokenUtil.getId();
        }
        chatId = ConnectionUtil.getChatId(
                RoleEnum.DOCTOR.getName() + doctorId,
                RoleEnum.PATIENT.getName() + patientId
        );
        String chatStringList = cacheService.get(chatId);
        List<Chat> chatList = JSON.parseArray(chatStringList, Chat.class);
        //没有这个键就加上
        if (!cacheService.exists(chatId)) {
            chatList = new ArrayList<>();
            cacheService.set(chatId, JSON.toJSONString(chatList));
        }

        if (chatList.size() > Constrants.CHAT_GET_BATCH_SIZE) {
            int startIndex = Math.max(0, chatList.size() - Constrants.CHAT_GET_BATCH_SIZE);
            return ResponseResult.okResult(chatList.subList(startIndex, chatList.size()));
        } else {
            int limit = Constrants.CHAT_GET_BATCH_SIZE - chatList.size();
            List<Chat> chatListHistory = this.list(
                    Wrappers.<Chat>lambdaQuery()
                            .and(wrapper ->
                                    wrapper
                                            .eq(Chat::getFromUserId, patientId)
                                            .eq(Chat::getFromUserRole, RoleEnum.PATIENT.getName())
                                            .eq(Chat::getToUserId, doctorId)
                                            .eq(Chat::getToUserRole, RoleEnum.DOCTOR.getName())
                            )
                            .or(wrapper ->
                                    wrapper
                                            .eq(Chat::getFromUserId, doctorId)
                                            .eq(Chat::getFromUserRole, RoleEnum.DOCTOR.getName())
                                            .eq(Chat::getToUserId, patientId)
                                            .eq(Chat::getToUserRole, RoleEnum.PATIENT.getName())
                            )
                            .orderByDesc(Chat::getTime)
                            .last("LIMIT " + limit)

            );
            chatList.addAll(chatListHistory);
            chatList = chatList.stream()
                    .sorted(Comparator.comparing(Chat::getTime)) // 按时间正序排序
                    .collect(Collectors.toList());
            return ResponseResult.okResult(chatList);
        }

    }

    public ResponseResult getHistory(HistoryMessageDTO historyChatDTO) {
        if (historyChatDTO.getStartTime() == null && historyChatDTO.getEndTime() == null)
            return ResponseResult.errorResult(400, "起止时间不可同时为空");

        Long patientId, doctorId;
        if (Objects.equals(SaTokenUtil.getRole(), RoleEnum.PATIENT.getName())) {
            patientId = SaTokenUtil.getId();
            doctorId = historyChatDTO.getOtherSideId();
        } else {
            patientId = historyChatDTO.getOtherSideId();
            doctorId = SaTokenUtil.getId();
        }

        String chatId = ConnectionUtil.getChatId(
                RoleEnum.DOCTOR.getName() + doctorId,
                RoleEnum.PATIENT.getName() + patientId
        );
        String chatStringList = cacheService.get(chatId);
        List<Chat> chatList = JSON.parseArray(chatStringList, Chat.class);
        //没有这个键就加上
        if (!cacheService.exists(chatId)) {
            chatList = new ArrayList<>();
            cacheService.set(chatId, JSON.toJSONString(chatList));
        }

        if (historyChatDTO.getStartTime() == null) {
            // 从 chatList 中选出时间属性小于指定值的项
            List<Chat> filteredChatList = chatList.stream()
                    .filter(chat -> chat.getTime().isBefore(historyChatDTO.getEndTime()))
                    .sorted((chat1, chat2) -> chat2.getTime().compareTo(chat1.getTime()))
                    .limit(Constrants.CHAT_GET_BATCH_SIZE)
                    .collect(Collectors.toList());
            int historyLeft = Constrants.CHAT_GET_BATCH_SIZE - filteredChatList.size();
            List<Chat> chatListHistory = this.list(
                    Wrappers.<Chat>lambdaQuery()
                            .and(wrapper ->
                                    wrapper
                                            .eq(Chat::getFromUserId, patientId)
                                            .eq(Chat::getFromUserRole, RoleEnum.PATIENT.getName())
                                            .eq(Chat::getToUserId, doctorId)
                                            .eq(Chat::getToUserRole, RoleEnum.DOCTOR.getName())
                                            .lt(Chat::getTime, historyChatDTO.getEndTime())
                            )
                            .or(wrapper ->
                                    wrapper
                                            .eq(Chat::getFromUserId, doctorId)
                                            .eq(Chat::getFromUserRole, RoleEnum.DOCTOR.getName())
                                            .eq(Chat::getToUserId, patientId)
                                            .eq(Chat::getToUserRole, RoleEnum.PATIENT.getName())
                                            .lt(Chat::getTime, historyChatDTO.getEndTime())
                            )
                            .orderByDesc(Chat::getTime)
                            .last("LIMIT " + historyLeft)

            );
            System.out.println(chatListHistory);
            chatListHistory.addAll(filteredChatList);
            chatListHistory = chatListHistory.stream()
                    .sorted(Comparator.comparing(Chat::getTime)) // 按时间正序排序
                    .collect(Collectors.toList());
            return ResponseResult.okResult(chatListHistory);
        } else if (historyChatDTO.getEndTime() == null) {
            // 从 chatList 中选出时间属性大于指定值的项
            List<Chat> filteredChatList = chatList.stream()
                    .filter(chat -> chat.getTime().isAfter(historyChatDTO.getStartTime())) // 过滤出时间大于指定值的项
                    .sorted(Comparator.comparing(Chat::getTime)) // 按时间升序排序
                    .limit(Constrants.CHAT_GET_BATCH_SIZE) // 限制返回的记录数为50条
                    .collect(Collectors.toList());
            if (filteredChatList.size() < chatList.size()) {
                return ResponseResult.okResult(filteredChatList);
            }

            int historyLeft = Constrants.CHAT_GET_BATCH_SIZE - filteredChatList.size();
            List<Chat> chatListHistory = this.list(
                    Wrappers.<Chat>lambdaQuery()
                            .and(wrapper ->
                                    wrapper
                                            .eq(Chat::getFromUserId, patientId)
                                            .eq(Chat::getFromUserRole, RoleEnum.PATIENT.getName())
                                            .eq(Chat::getToUserId, doctorId)
                                            .eq(Chat::getToUserRole, RoleEnum.DOCTOR.getName())
                                            .gt(Chat::getTime, historyChatDTO.getStartTime())
                            )
                            .or(wrapper ->
                                    wrapper
                                            .eq(Chat::getFromUserId, doctorId)
                                            .eq(Chat::getFromUserRole, RoleEnum.DOCTOR.getName())
                                            .eq(Chat::getToUserId, patientId)
                                            .eq(Chat::getToUserRole, RoleEnum.PATIENT.getName())
                                            .gt(Chat::getTime, historyChatDTO.getStartTime())
                            )
                            .orderByDesc(Chat::getTime)
                            .last("LIMIT " + historyLeft)

            );
            chatListHistory.addAll(filteredChatList);
            chatListHistory = chatListHistory.stream()
                    .sorted(Comparator.comparing(Chat::getTime)) // 按时间正序排序
                    .collect(Collectors.toList());
            return ResponseResult.okResult(chatListHistory);
        } else {
            // 从 chatList 中选出时间属性在指定时间区间的项
            List<Chat> filteredChatList = chatList.stream()
                    .filter(chat ->
                            (chat.getTime().equals(historyChatDTO.getStartTime()) || chat.getTime().isAfter(historyChatDTO.getStartTime())) &&
                                    (chat.getTime().equals(historyChatDTO.getEndTime()) || chat.getTime().isBefore(historyChatDTO.getEndTime()))) // 过滤出时间在指定区间内的项
                    .collect(Collectors.toList());

            List<Chat> chatListHistory = this.list(
                    Wrappers.<Chat>lambdaQuery()
                            .and(wrapper ->
                                    wrapper
                                            .eq(Chat::getFromUserId, patientId)
                                            .eq(Chat::getFromUserRole, RoleEnum.PATIENT.getName())
                                            .eq(Chat::getToUserId, doctorId)
                                            .eq(Chat::getToUserRole, RoleEnum.DOCTOR.getName())
                                            .between(Chat::getTime, historyChatDTO.getStartTime(), historyChatDTO.getEndTime())
                            )
                            .or(wrapper ->
                                    wrapper
                                            .eq(Chat::getFromUserId, doctorId)
                                            .eq(Chat::getFromUserRole, RoleEnum.DOCTOR.getName())
                                            .eq(Chat::getToUserId, patientId)
                                            .eq(Chat::getToUserRole, RoleEnum.PATIENT.getName())
                                            .between(Chat::getTime, historyChatDTO.getStartTime(), historyChatDTO.getEndTime())
                            )

            );
            chatListHistory.addAll(filteredChatList);
            chatListHistory = chatListHistory.stream()
                    .sorted(Comparator.comparing(Chat::getTime)) // 按时间正序排序
                    .collect(Collectors.toList());
            return ResponseResult.okResult(chatListHistory);
        }

    }
}
