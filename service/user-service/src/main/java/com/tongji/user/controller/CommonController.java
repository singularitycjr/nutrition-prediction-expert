package com.tongji.user.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.alibaba.fastjson.JSON;
import com.tongji.common.constants.CommonConstants;
import com.tongji.common.enums.AppHttpCodeEnum;
import com.tongji.common.service.FileStorageService;
import com.tongji.common.service.Impl.CacheService;
import com.tongji.model.vo.GoBankUploadVO;
import com.tongji.model.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@RestController
//@SaCheckLogin
@RequestMapping("/common")
@Tag(name = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private CacheService cacheService;

//    @Operation(summary = "上传文件")
//    @PostMapping("/upload")
//    public ResponseResult upload(MultipartFile file){
//        log.info("上传文件：" + file.getName());
//
//        if(file == null || file.getSize() == 0){
//            return ResponseResult.errorResult(AppHttpCodeEnum.UPLOAD_FAILED);
//        }
//
//        //2.上传图片到minIO中
//        String fileName = UUID.randomUUID().toString().replace("-", "");
//        //aa.jpg
//        String originalFilename = file.getOriginalFilename();
//        assert originalFilename != null;
//        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String fileId = null;
//        try {
//            fileId = fileStorageService.uploadImgFile("", fileName + postfix, file.getInputStream());
//            log.info("上传图片到MinIO中，fileId:{}",fileId);
//        } catch (Exception e) {
//            log.error("WmMaterialServiceImpl-上传文件失败 {}", e.getMessage());
//        }
//
//        //4.返回结果
//
//        return ResponseResult.okResult(fileId);
//    }

    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile file){
        log.info("上传文件：" + file.getName());
        if(file.getSize() == 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.UPLOAD_FAILED);
        }

        String fileId=fileStorageService.uploadFile(file);

        return ResponseResult.okResult(fileId);
    }


    @Operation(summary = "上传Mask图片")
    @PostMapping("/uploadMask")
    public ResponseResult uploadMask(MultipartFile file){

        if(file == null || file.getSize() == 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.UPLOAD_FAILED);
        }

        //2.上传图片到minIO中
        String fileName = UUID.randomUUID().toString().replace("-", "");
        //aa.jpg
        String originalFilename = file.getOriginalFilename();
        log.info("originalFilename:{}",originalFilename);

        assert originalFilename != null;
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileId = null;
        try {
            fileId = fileStorageService.uploadImgFile("", fileName + postfix, file.getInputStream());
            log.info("上传图片到MinIO中，fileId:{}",fileId);
            String[] split = originalFilename.split("/");
            String imgName = split[split.length - 1];
            // 去掉后缀
            imgName = imgName.substring(0, imgName.lastIndexOf("."));
            imgName = imgName.substring(0, imgName.lastIndexOf("_"));
            String userId = this.cacheService.get(CommonConstants.FOOD_IMG + imgName);
            // 删掉这条记录
            this.cacheService.delete(CommonConstants.FOOD_IMG + imgName);
            // 把userId对应的mask图片存入缓存
            this.cacheService.setEx(CommonConstants.USER_MASK_IMG + userId,
                    fileId,
                    CommonConstants.USER_IMG_TIMEOUT,
                    CommonConstants.USER_IMG_TIMEOUT_TYPE);
        } catch (Exception e) {
            log.error("WmMaterialServiceImpl-上传文件失败 {}", e.getMessage());
        }

        //4.返回结果

        return ResponseResult.okResult(fileId);
    }

    @Operation(summary = "测试接口")
    @GetMapping("/ping")
    public ResponseResult test() {
        return ResponseResult.okResult("pong");
    }

    @Operation(summary = "上传文件到go后端")
    @PostMapping("/uploadToGo")
    public ResponseResult uploadToGo(@RequestBody byte[] body, @RequestHeader HttpHeaders headers){
        // 获取请求头中的 content-type
        String contentType = Objects.requireNonNull(headers.getContentType()).toString();

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://ericwvi.site/bgmp/api/files/upload?Name=avatar";

        // 为restTemplate添加请求头
        /* 请求头 */
        HttpHeaders header = new HttpHeaders();

        header.add("x-api-key", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Nn0.0C-pklGl8S9T6aiJgxZAudKW3x2gbisxKUaxiRj2WbA");
        header.add("Content-Type", contentType);
        HttpEntity<byte[]> httpEntity = new HttpEntity<>(body, header);

        String res = restTemplate.postForEntity(url, httpEntity, String.class).getBody();

        GoBankUploadVO goBankEndVO = JSON.parseObject(res, GoBankUploadVO.class);

        if (goBankEndVO == null || goBankEndVO.getCode() == null || goBankEndVO.getCode() != 200) {
            return ResponseResult.errorResult(AppHttpCodeEnum.UPLOAD_FAILED);
        }
        return ResponseResult.okResult(goBankEndVO.getMessage());
    }
}
