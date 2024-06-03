package com.tongji.global.util;

import com.tongji.global.constrants.Constrants;
import lombok.Data;

@Data
public class ConnectionUtil {
    //用于redis暂存消息和聊天记录

    public static String getChatId(String userId1, String userId2)
    {
        if (userId1.compareTo(userId2) < 0) {
            // userId1 小于 userId2
            return Constrants.CHAT+userId1 + "_" + userId2;
        } else {
            // userId1 大于等于 userId2
            return Constrants.CHAT+ userId2 + "_" + userId1;
        }
    }

    public static String getMessageId(String userId1,String userId2)
    {
        if (userId1.compareTo(userId2) < 0) {
            // userId1 小于 userId2
            return Constrants.MESSAGE+userId1 + "_" + userId2;
        } else {
            // userId1 大于等于 userId2
            return Constrants.MESSAGE+ userId2 + "_" + userId1;
        }
    }
}
