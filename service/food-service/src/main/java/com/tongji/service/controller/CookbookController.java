package com.tongji.service.controller;


import com.tongji.model.dto.patient.CookbookDTO;
import com.tongji.model.vo.ResponseResult;
import com.tongji.service.service.ICookbookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2023-12-18
 */
@RestController
//@SaCheckRole("PATIENT")
@RequestMapping("/cookbook")
@Tag(name = "食谱数据")
public class CookbookController {
    @Autowired
    private ICookbookService cookbookService;

    @GetMapping("/getCookbook")
    @Operation(summary = "获取食谱信息")
    public ResponseResult getCookbook(CookbookDTO cookbookDTO){
        return this.cookbookService.getCookbook(cookbookDTO);
    }

}
