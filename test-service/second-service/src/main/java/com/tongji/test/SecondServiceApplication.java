package com.tongji.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SecondServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecondServiceApplication.class,args);
        log.info("service启动成功");
    }
}
