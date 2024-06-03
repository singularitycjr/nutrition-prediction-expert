package com.tongji.model.dto.websocket;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MessageDTO implements Serializable {
    private Long id;
    private Long fromUserId;
    private String fromUserRole;
    private String fromUserName;
    private Long toUserId;
    private String toUserRole;
    private String toUserName;
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime time;
    private Boolean confirmed;
}
