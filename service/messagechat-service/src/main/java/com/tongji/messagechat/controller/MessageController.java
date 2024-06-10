package com.tongji.messagechat.controller;


import com.tongji.model.query.MessageQuery;
import com.tongji.model.vo.ResponseResult;
import com.tongji.messagechat.service.IMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@Tag(name = "消息接口")
@Slf4j
public class MessageController {
    @Autowired
    IMessageService messageService;


//    @Operation(summary = "获取最新若干条数据")
//    @GetMapping("/getLatest")
//    public ResponseResult getLatest(LatestMessageDTO latestChatDTO){
//        return messageService.getLatest(latestChatDTO);
//    }

    @Operation(summary = "获取历史消息数据",
            description = "分页查询，默认一页5条")
    @GetMapping("/getHistory")
    public ResponseResult getHistory(MessageQuery messageQuery){
        return messageService.getHistory(messageQuery);
    }

    @Operation(summary = "获取最新若干条未读数据")
    @GetMapping("/getLatestUnread")
    public ResponseResult getLatestUnread(){
        return messageService.getLatestUnread();
    }

    @Operation(summary = "获取指定时间前后若干条未读数据",
            description = "分页查询，默认一页5条")
    @GetMapping("/getHistoryUnread")
    public ResponseResult getHistoryUnread(MessageQuery messageQuery){
        return messageService.getHistoryUnread(messageQuery);
    }

    @Operation(summary = "获取未读消息数目")
    @GetMapping("/getUnreadNum")
    public ResponseResult getUnreadNum(){
        return messageService.getUnreadNum();
    }

    @Operation(summary = "获取最新若干条已发送数据")
    @GetMapping("/getLatestSend")
    public ResponseResult getLatestSend(){
        return messageService.getLatestSend();
    }

    @Operation(summary = "获取指定时间前后若干条已发送数据",
            description = "分页查询，默认一页5条")
    @GetMapping("/getHistorySend")
    public ResponseResult getHistorySend(MessageQuery messageQuery){
        return messageService.getHistorySend(messageQuery);
    }

    @Operation(summary = "获取已发送消息数目")
    @GetMapping("/getSendNum")
    public ResponseResult getSendNum(){
        return messageService.getSendNum();
    }

    @Operation(summary = "确认某条消息",
            description = "传入一个消息id数组，只能是发信人或收信人是自己的消息")
    @PostMapping("/confirm")
    public ResponseResult confirm(@RequestBody List<Long> idList){
        return messageService.confirm(idList);
    }

//    @Operation(summary = "删除某条消息",
//            description = "传入一个消息id数组，只能是发信人或收信人是自己的消息")
//    @PostMapping("/delete")
//    public ResponseResult delete(@RequestBody List<Long> idList){
//        return messageService.delete(idList);
//    }
}
