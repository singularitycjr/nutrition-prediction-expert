package com.tongji.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecordUpdateDTO {
    private Long id;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    /**
     * 类型（早午晚加餐）
     */
    private String type;
}
