package com.tongji.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/second")
public class SecondController {

        @RequestMapping("/hello")
        public String hello(){
            return "hello second service";
        }
}
