package com.tongji.messagechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.pojo.Message;
import com.tongji.model.query.MessageQuery;
import com.tongji.model.vo.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IMessageService extends IService<Message> {
//    ResponseResult getLatest(LatestMessageDTO latestChatDTO);
    ResponseResult getHistory(MessageQuery messageQuery);
    ResponseResult getLatestUnread();
    ResponseResult getHistoryUnread(MessageQuery messageQuery);
    ResponseResult getUnreadNum();
    ResponseResult getLatestSend();
    ResponseResult getHistorySend(MessageQuery messageQuery);
    ResponseResult getSendNum();
    ResponseResult confirm( List<Long> idList);
    ResponseResult delete( List<Long> idList);
}
