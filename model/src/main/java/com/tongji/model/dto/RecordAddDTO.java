package com.tongji.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecordAddDTO {

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 图片链接
     */
    private String imgUrl;

    /**
     * 类型（早午晚加餐）
     */
    private String type;
}
