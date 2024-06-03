package com.tongji.messagechat;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tongji.messagechat.mapper")
@Slf4j
public class MessagechatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessagechatServiceApplication.class,args);
        log.info("messagechat启动成功");
        log.info("http://localhost:8026/doc.html");
    }
}
