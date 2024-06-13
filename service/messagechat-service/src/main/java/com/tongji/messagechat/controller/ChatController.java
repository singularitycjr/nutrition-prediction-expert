package com.tongji.messagechat.controller;


import com.tongji.model.dto.messagechat.ReadTimeDTO;
import com.tongji.model.dto.websocket.HistoryMessageDTO;
import com.tongji.model.vo.ResponseResult;
import com.tongji.messagechat.service.IChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@Tag(name = "聊天接口")
@Slf4j
public class ChatController {
    @Autowired
    IChatService chatService;


    @Operation(summary = "获取最新若干条数据",
            description = "要传对方的id，返回之多50条最近的数据。注意，展示聊天记录时先调这个接口，然后调getHistory")
    @GetMapping("/getLatest/{otherSideId}")
    public ResponseResult getLatest(@PathVariable("otherSideId") Long otherSideId) {
        return chatService.getLatest(otherSideId);
    }

    @Operation(summary = "获取指定时间前后若干条数据",
            description = "要传对方的id，如果只传startTime则返回开始时间之后五十条数据（不包含边界值），" +
                    "只传endTime则返回结束时间之前50条数据（不包含边界值）." +
                    "如果起止时间都传，则一次性返回这段时间所有聊天记录（包含边界值）." +
                    "聊天记录逻辑应是，一开始调getLatest获取最新记录，之后聊天页面往上滑，滑到最顶上调getHistory接口，传当前前端能拿到的最早的记录时间，获取更早的记录." +
                    "如果要做查看历史聊天记录，则是传入某个整天0点到23:59的数据，或者只传0时，之后随着页面下拉调这个接口传能拿到的最晚消息时间，逐步获取后面的记录，以防一次返回的太多"
    )
    @GetMapping("/getHistory")
    public ResponseResult getHistory(HistoryMessageDTO historyChatDTO) {
        return chatService.getHistory(historyChatDTO);
    }

    @Operation(summary = "更新已读时间")
    @PostMapping("/updateReadTime")
    public ResponseResult updateReadTime(@RequestBody ReadTimeDTO readTimeDTO) {
        return chatService.updateReadTime(readTimeDTO);
    }

    @Operation(summary = "获取未读数目",
            description = "传入正在聊天的对方的id")
    @GetMapping("/getUnreadNum/{otherSideId}")
    public ResponseResult getUnreadNum(@PathVariable("otherSideId") Long otherSideId) {
        return chatService.getUnreadNum(otherSideId);
    }
}
