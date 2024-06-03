package com.tongji.global.enums;

import lombok.Getter;

@Getter
public enum MessageTypeEnum {
    MESSAGE(1L),
    CHAT(2L);

    private final Long type;

    MessageTypeEnum(Long type) {
        this.type = type;
    }

    public Long value(){
        return type;
    }
}
