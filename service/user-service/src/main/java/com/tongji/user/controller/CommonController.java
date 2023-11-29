package com.tongji.user.controller;

import com.tongji.common.enums.AppHttpCodeEnum;
import com.tongji.common.service.FileStorageService;
import com.tongji.model.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/common")
@Tag(name = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private FileStorageService fileStorageService;

    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile file){
        log.info("上传文件：" + file.getName());

        if(file == null || file.getSize() == 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.UPLOAD_FAILED);
        }

        //2.上传图片到minIO中
        String fileName = UUID.randomUUID().toString().replace("-", "");
        //aa.jpg
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileId = null;
        try {
            fileId = fileStorageService.uploadImgFile("", fileName + postfix, file.getInputStream());
            log.info("上传图片到MinIO中，fileId:{}",fileId);
        } catch (Exception e) {
            log.error("WmMaterialServiceImpl-上传文件失败 {}", e.getMessage());
        }

        //4.返回结果

        return ResponseResult.okResult(fileId);
    }


}
