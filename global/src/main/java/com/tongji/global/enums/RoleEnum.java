package com.tongji.global.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum RoleEnum {
    PATIENT(1L,"PATIENT"),
    DOCTOR(1L<<1,"DOCTOR");

    private final Long roleNum;
    private final String name;

    RoleEnum(Long roleNum, String name){
        this.roleNum = roleNum;
        this.name = name;
    }

    private static final Map<Long,RoleEnum> mapRoleEnum = new HashMap<>();
    static {
        for (RoleEnum value : RoleEnum.values()) {
            mapRoleEnum.put(value.getRoleNum(),value);
        }
    }

    static public RoleEnum find(Long roleNum){
        return mapRoleEnum.get(roleNum);
    }

//    public static String getName(RoleEnum roleEnum){
//        return roleEnum.getName();
//    }
}
