package com.tongji.service.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "algorithm")  // 文件上传 配置前缀file.oss
public class AlgorithmProperties {
    private String host;
    private String createMaskPort;
    private String useMaskPort;

    private Boolean enableAlgorithm;
}
