package com.tongji.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class Message {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消息来源方Id
     */
    private Long fromUserId;

    /**
     * 消息来源方角色
     */
    private String fromUserRole;

    /**
     * 消息接收方Id
     */
    private Long toUserId;

    /**
     * 消息接收方角色
     */
    private String toUserRole;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime time;

    /**
     * 是否确认
     */
    private Boolean confirmed;
}