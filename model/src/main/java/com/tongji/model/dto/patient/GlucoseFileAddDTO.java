package com.tongji.model.dto.patient;

import lombok.Data;

@Data
public class GlucoseFileAddDTO {
    private String url;
    private Long valueCol;
    private Long timeCol;
}
