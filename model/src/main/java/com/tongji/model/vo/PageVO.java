package com.tongji.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {
    private Long total;
    private Long pages;
    private List<T> list;
}
