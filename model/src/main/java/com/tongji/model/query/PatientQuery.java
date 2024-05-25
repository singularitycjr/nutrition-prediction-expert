package com.tongji.model.query;

import lombok.Data;

@Data
public class PatientQuery extends PageQuery{
    //电话号码，即账号
    private String account;

    //姓名
    private String name;
}
