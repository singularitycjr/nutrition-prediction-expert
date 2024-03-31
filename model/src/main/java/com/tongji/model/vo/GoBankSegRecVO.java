package com.tongji.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class GoBankSegRecVO {
    @Data
    public static class Result {
        Integer food;
        String region;
        List<String> top5;
    }
    @Data
    public static class Message {
        String dietId;
        List<Result> results;
    }

    String requestId;
    Integer code;
    Message message;
}
