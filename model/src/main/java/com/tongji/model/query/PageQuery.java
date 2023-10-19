package com.tongji.model.query;

import lombok.Data;


@Data
public class PageQuery {
    private Integer pageNo;
    private Integer pageSize;
    private String orderBy;
    private Boolean isAsc;
}
