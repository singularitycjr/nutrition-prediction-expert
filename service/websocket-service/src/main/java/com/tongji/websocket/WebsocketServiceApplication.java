package com.tongji.websocket;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.tongji.messagechat.mapper")
@ComponentScan( {"com.tongji.websocket","com.tongji.messagechat.service"})
@Slf4j
public class WebsocketServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketServiceApplication.class,args);
        log.info("websocket启动成功");
//        log.info("http://localhost:8025/doc.html");
    }
}
