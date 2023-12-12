package com.tongji.model.dto;

import lombok.Data;

@Data
public class UserDTO {
    /**
     * 姓名
     */
    private String name;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String rePassword;

    /**
     * 验证码
     */
    private String code;
}
