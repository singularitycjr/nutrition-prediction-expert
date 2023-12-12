package com.tongji.user.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.tongji.model.dto.LoginDTO;
import com.tongji.model.dto.UserDTO;
import com.tongji.model.pojo.UserDetail;
import com.tongji.model.vo.ResponseResult;
import com.tongji.user.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResponseResult getUser(){
        //return userService.getById(1).toString();
        return ResponseResult.okResult(userService.getById(1));
    }

    @PostMapping("/login")
    @Operation(summary = "登录")
    public ResponseResult login(@RequestBody LoginDTO dto){
        return userService.login(dto);
    }

    @PostMapping("/logout")
    @Operation(summary = "登出")
    public ResponseResult logout(){
        StpUtil.logout();
        return ResponseResult.okResult("登出成功");
    }

    @Operation(summary = "注册用户")
    @PostMapping("/register")
    public ResponseResult register(@RequestBody UserDTO dto){
        return userService.register(dto);
    }

    @Operation(summary = "发送短信验证码")
    @GetMapping("/sendCode/{mobile}")
    public ResponseResult sendCode(@PathVariable("mobile") String mobile){
        return userService.sendCode(mobile);
    }

    @Operation(summary = "修改信息时发送短信验证码")
    @GetMapping("/sendCodeUpdate/{mobile}")
    public ResponseResult sendCodeUpdate(@PathVariable("mobile") String mobile){
        return userService.sendCodeUpdate(mobile);
    }

    @Operation(summary = "修改用户名")
    @PutMapping("/updateName")
    public ResponseResult updateName(@RequestBody UserDTO dto){
        return userService.updateName(dto);
    }

    @Operation(summary = "修改密码")
    @PutMapping("/updatePassword")
    public ResponseResult updatePassword(@RequestBody UserDTO dto){
        return userService.updatePassword(dto);
    }

    @Operation(summary = "忘记密码")
    @PutMapping("/forgetPassword")
    public ResponseResult forgetPassword(@RequestBody UserDTO dto){
        return userService.forgetPassword(dto);
    }

    @Operation(summary = "获取用户细节信息(只能获取自己的)")
    @GetMapping("/getDetail")
    public ResponseResult getDetail(){
        return userService.getDetail();
    }

    @Operation(summary = "修改用户细节信息(只能修改自己的)")
    @PutMapping("/updateDetail")
    public ResponseResult updateDetail(@RequestBody UserDetail userDetail){
        return userService.updateDetail(userDetail);
    }
}
