package com.tongji.messagechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.dto.websocket.HistoryMessageDTO;
import com.tongji.model.pojo.Chat;
import com.tongji.model.vo.ResponseResult;

public interface IChatService extends IService<Chat> {
    ResponseResult getLatest(Long otherSideId);
    ResponseResult getHistory(HistoryMessageDTO historyChatDTO);

}
