package com.tongji.doctor.controller;


import com.tongji.model.vo.ResponseResult;
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
@RequestMapping("/temp")
@Tag(name = "食谱数据数据")
public class TempController {

    @GetMapping("/temp")
    @Operation(summary = "测试")
    public ResponseResult temp(){
        return null;
    }

}
