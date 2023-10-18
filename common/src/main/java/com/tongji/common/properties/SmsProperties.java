package com.tongji.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sms")
@Data
public class SmsProperties {
    private String appId;
    private String appSecret;
    private String templateId;
}
