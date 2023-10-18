package com.tongji.common.utils;

import com.tongji.common.properties.SmsProperties;
import com.zhenzi.sms.ZhenziSmsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SmsUtil {

    @Autowired
    private SmsProperties smsProperties;

    public void sendSms(String phone, String code) {
        // 使用自己的 AppId 和 AppSecret
        ZhenziSmsClient client = new ZhenziSmsClient("https://sms_developer.zhenzikj.com",
                this.smsProperties.getAppId(),
                this.smsProperties.getAppSecret());
        Map<String, Object> params = new HashMap<>();
        params.put("number", phone);
        // 修改为自己的templateId
        params.put("templateId", this.smsProperties.getTemplateId());
        String[] templateParams = new String[2];
        templateParams[0] = code;
        templateParams[1] = "3";
        params.put("templateParams", templateParams);
        try {
            String result = client.send(params);
            log.info("发送短信验证码，结果为：{}", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
