package com.tongji.global.helper;

import lombok.Data;

@Data
public class LoginObj {
    private String role;
    private Long id;

    public LoginObj(String role, Long id){
        this.role=role;
        this.id=id;
    }
}
