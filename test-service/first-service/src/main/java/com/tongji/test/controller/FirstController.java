package com.tongji.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/first")
public class FirstController {

    @RequestMapping("/hello")
    public String hello(){
        return "hello first service";
    }
}
