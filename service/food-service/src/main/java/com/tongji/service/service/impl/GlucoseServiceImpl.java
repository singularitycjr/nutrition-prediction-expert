package com.tongji.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tongji.common.service.FileStorageService;
import com.tongji.model.dto.*;
import com.tongji.model.pojo.Glucose;
import com.tongji.model.vo.ResponseResult;
import com.tongji.service.mapper.GlucoseMapper;
import com.tongji.service.service.IGlucoseService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-11-29
 */
@Service
public class GlucoseServiceImpl extends ServiceImpl<GlucoseMapper, Glucose> implements IGlucoseService {

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public ResponseResult getGlucose(TimeRangeDTO timeRangeDTO) {
        if (timeRangeDTO.getStartTime() == null || timeRangeDTO.getEndTime() == null) {
            return ResponseResult.errorResult(400, "时间范围不能为空");
        }
        Long id = StpUtil.getLoginIdAsLong();
        List<Glucose> glucoseList = this.list(
                Wrappers.<Glucose>lambdaQuery().eq(Glucose::getUserId, id).
                        between(Glucose::getTime, timeRangeDTO.getStartTime(), timeRangeDTO.getEndTime())
        );
        return ResponseResult.okResult(glucoseList);
    }

    @Override
    public ResponseResult deleteGlucose(Long id) {
        if (id == null) {
            return ResponseResult.errorResult(400, "id不能为空");
        }
        Long userId = StpUtil.getLoginIdAsLong();
        Glucose glucose = this.getOne(
                Wrappers.<Glucose>lambdaQuery().eq(Glucose::getUserId, userId).
                        eq(Glucose::getId, id)
        );
        if (glucose == null) {
            return ResponseResult.errorResult(400, "该血糖数据不存在");
        }
        this.removeById(id);
        return ResponseResult.okResult("删除成功");
    }

    @Override
    public ResponseResult updateGlucose(GlucoseDTO glucoseDTO) {
        if (glucoseDTO.getId() == null) {
            return ResponseResult.errorResult(400, "id不能为空");
        }
        Long userId = StpUtil.getLoginIdAsLong();
        Glucose glucose = this.getOne(
                Wrappers.<Glucose>lambdaQuery().eq(Glucose::getUserId, userId).
                        eq(Glucose::getId, glucoseDTO.getId())
        );
        if (glucose == null) {
            return ResponseResult.errorResult(400, "该血糖数据不存在");
        }
        glucose.setGluValue(glucoseDTO.getGluValue());
        glucose.setTime(glucoseDTO.getTime());
        this.updateById(glucose);
        return ResponseResult.okResult("修改成功");
    }

    @Override
    public ResponseResult addGlucose(GlucoseAddDTO glucoseAddDTO) {
        if (glucoseAddDTO.getGluValue() == null || glucoseAddDTO.getTime() == null) {
            return ResponseResult.errorResult(400, "血糖值和时间不能为空");
        }
        Long userId = StpUtil.getLoginIdAsLong();
        Glucose glucose = new Glucose();
        glucose.setId(null);
        glucose.setUserId(userId);
        glucose.setGluValue(glucoseAddDTO.getGluValue());
        glucose.setTime(glucoseAddDTO.getTime());
        this.save(glucose);
        return ResponseResult.okResult("添加成功");
    }

    @Override
    public ResponseResult uploadGlucoseFile(MultipartFile file) {
        if(file.isEmpty()){
            return ResponseResult.errorResult(400, "上传文件不能为空");
        }

        //默认是xlsx
        Workbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            return ResponseResult.errorResult(400,"文件读取错误");
        }
        Sheet sheet = workbook.getSheetAt(0);
        Row sheetTitleRow = sheet.getRow(sheet.getFirstRowNum());
        GlucoseFileReturnDTO glucoseFileReturnDTO = new GlucoseFileReturnDTO();

        short lastCellNum = sheetTitleRow.getLastCellNum();
        List<String> list=glucoseFileReturnDTO.getTitleRow();
        for (int i = 0; i < lastCellNum; i++) {
            // 取出每一列的名
            String cellValue = sheetTitleRow.getCell(i).getStringCellValue();
            list.add(cellValue);
        }

        //将文件上传到minio
        glucoseFileReturnDTO.setPath(fileStorageService.uploadFile(file)) ;
        return ResponseResult.okResult(glucoseFileReturnDTO);

    }

    @Override
    public ResponseResult addFileData(GlucoseFileAddDTO glucoseFileAddDTO)
    {
        if(glucoseFileAddDTO.getUrl()==null||glucoseFileAddDTO.getTimeCol()==null
                ||glucoseFileAddDTO.getValueCol()==null)
            return ResponseResult.errorResult(400, "文件路径、血糖列、时间列不能为空");

        InputStream inputStream = new ByteArrayInputStream(fileStorageService.downLoadFile(glucoseFileAddDTO.getUrl()));
        Workbook workbook;
        try {
            workbook=new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            return ResponseResult.okResult(400,"文件下载错误");
        }

        Sheet sheet=workbook.getSheetAt(0);
        for(int i = 1; i<=sheet.getLastRowNum() ; i++) {
            Row row=sheet.getRow(i);
            Cell timeCell=row.getCell(glucoseFileAddDTO.getTimeCol().intValue());
            Cell valueCell=row.getCell(glucoseFileAddDTO.getValueCol().intValue());
            if(timeCell == null || valueCell==null||
                    timeCell.getCellType().equals(CellType.BLANK)||valueCell.getCellType().equals(CellType.BLANK))
            {
                sheet.removeRow(row);
            }

        }

        Iterator<Row> rowIterator=sheet.rowIterator();
//        System.out.println(sheet.getLastRowNum());
        //跳过第一行
        if(rowIterator.hasNext())
        rowIterator.next();
        else
            return ResponseResult.okResult(400,"表格文件为空");

        Long userId = StpUtil.getLoginIdAsLong();
        List<Glucose> glucoseList=new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        while(rowIterator.hasNext())
        {
            Row row=rowIterator.next();
            LocalDateTime parsedDate;
            BigDecimal value;
            try {
                Date date=row.getCell(glucoseFileAddDTO.getTimeCol().intValue()).getDateCellValue();
                value=BigDecimal.valueOf(row.getCell(glucoseFileAddDTO.getValueCol().intValue()).getNumericCellValue());
                value=value.divide(BigDecimal.valueOf(18),3, RoundingMode.HALF_UP);
                parsedDate= date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
            catch (Exception e){
                System.out.println(e);
                return ResponseResult.okResult(400,"文件数据格式错误");
            }

            Glucose glucose=new Glucose();
            glucose.setId(null);
            glucose.setUserId(userId);
            glucose.setTime(parsedDate );
            glucose.setGluValue(value);

            glucoseList.add(glucose);
        }
        this.saveBatch(glucoseList);

        return ResponseResult.okResult(200,"文件上传成功");
    }

}
