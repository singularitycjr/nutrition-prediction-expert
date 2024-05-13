package com.tongji.model.dto.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Data
public class GlucoseFileReturnDTO {
    private String path;

    private List<String> titleRow;

    public GlucoseFileReturnDTO(){
        titleRow=new ArrayList<>();
    }
}
