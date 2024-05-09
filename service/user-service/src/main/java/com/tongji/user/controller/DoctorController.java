package com.tongji.user.controller;


import com.tongji.model.dto.DoctorDTO;
import com.tongji.model.dto.DoctorLoginDTO;
import com.tongji.model.vo.ResponseResult;
import com.tongji.user.service.IDoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 员工信息 前端控制器
 * </p>
 *
 * @author author
 * @since 2023-10-18
 */
@RestController
@RequestMapping("/doctor")
@Tag(name = "用户")
public class DoctorController {
    @Autowired
    private IDoctorService doctorService;

    @GetMapping("/get")
    @Operation(summary = "获取用户信息")
    public ResponseResult getDoctor(){
        return doctorService.getDoctor();
    }


    @PostMapping("/login")
    @Operation(summary = "登录")
    public ResponseResult login(@RequestBody DoctorLoginDTO dto){
        return doctorService.login(dto);
    }

    @PostMapping("/logout")
    @Operation(summary = "登出")
    public ResponseResult logout(){
        return doctorService.logout();
    }


    @Operation(summary = "注册用户")
    @PostMapping("/register")
    public ResponseResult register(@RequestBody DoctorDTO dto){
        return doctorService.register(dto);
    }

    @Operation(summary = "发送短信验证码")
    @GetMapping("/sendCode/{mobile}")
    public ResponseResult sendCode(@PathVariable("mobile") String mobile){
        return doctorService.sendCode(mobile);
    }

    @Operation(summary = "修改信息时发送短信验证码")
    @GetMapping("/sendCodeUpdate/{mobile}")
    public ResponseResult sendCodeUpdate(@PathVariable("mobile") String mobile){
        return doctorService.sendCodeUpdate(mobile);
    }

    @Operation(summary = "修改用户名")
    @PutMapping("/updateName")
    public ResponseResult updateName(@RequestBody DoctorDTO dto){
        return doctorService.updateName(dto);
    }

    @Operation(summary = "修改密码")
    @PutMapping("/updatePassword")
    public ResponseResult updatePassword(@RequestBody DoctorDTO dto){
        return doctorService.updatePassword(dto);
    }

    @Operation(summary = "忘记密码")
    @PutMapping("/forgetPassword")
    public ResponseResult forgetPassword(@RequestBody DoctorDTO dto){
        return doctorService.forgetPassword(dto);
    }


    @Operation(summary = "上传头像")
    @PostMapping("/uploadProfix")
    public ResponseResult uploadProfix(@RequestParam("file") MultipartFile picture){
        return doctorService.uploadProfix(picture);
    }


}
