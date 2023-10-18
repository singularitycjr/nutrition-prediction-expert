package com.tongji.service.controller;


import com.tongji.service.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 员工信息 前端控制器
 * </p>
 *
 * @author author
 * @since 2023-10-18
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/get")
    @Operation(summary = "获取用户信息")
    public String getUser(){
        return userService.getById(1).toString();
    }

}
