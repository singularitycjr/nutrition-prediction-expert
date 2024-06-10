package com.tongji.model.dto.common;

import lombok.Data;

@Data
public class GlucoseFileAddDTO {
    private String url;
    private Long timeCol;
    private Long valueCol;

}
