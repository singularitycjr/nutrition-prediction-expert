package com.tongji.model.dtos;

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
     * 验证码
     */
    private String code;
}
