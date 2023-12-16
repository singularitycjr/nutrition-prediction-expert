package com.tongji.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecordDTO {

    private Long id;

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
