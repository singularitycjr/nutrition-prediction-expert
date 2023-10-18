package com.tongji.service.controller;

import com.tongji.common.constants.OssConstants;
import com.tongji.common.enums.AppHttpCodeEnum;
import com.tongji.common.utils.AliOssUtil;
import com.tongji.model.dtos.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Tag(name = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile file){
        log.info("上传文件：" + file.getName());
        try {
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = UUID.randomUUID() + extension;

            String filePath = this.aliOssUtil.upload(file.getBytes(), objectName, OssConstants.FOLDER);

            return ResponseResult.okResult(filePath);
        } catch (IOException e) {
            log.info("上传失败：" + e.getMessage());
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.UPLOAD_FAILED);
    }


}
