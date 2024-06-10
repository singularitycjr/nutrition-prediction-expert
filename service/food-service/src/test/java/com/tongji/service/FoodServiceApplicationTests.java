package com.tongji.service;

import com.tongji.model.dto.patient.RecordDetailAddDTO;
import com.tongji.model.vo.ResponseResult;
import com.tongji.service.service.IRecordDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FoodServiceApplicationTests {
    @Autowired
    IRecordDetailService recordDetailService;

    @Test
    public void case1() {
        RecordDetailAddDTO recordDetailDTO=new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(1000L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.5200));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(0.0420));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(3.4340));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(0));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(16.2340));
        ResponseResult responseResult=recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("RecordId不存在", responseResult.getMsg());
    }
    @Test
    public void case2() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        try {
            Field field = recordDetailDTO.getClass().getDeclaredField("recordId");
            field.setAccessible(true);
            field.set(recordDetailDTO, "abc");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        recordDetailDTO.setRecordId("abc");
        recordDetailDTO.setFoodId(2L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.5200));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(0.0420));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(3.4340));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(0));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(16.2340));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("RecordId不能为空", responseResult.getMsg());
    }

    @Test
    public void case3() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(null);
        recordDetailDTO.setFoodId(2L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.5200));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(0.0420));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(3.4340));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(0));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(16.2340));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("RecordId不能为空", responseResult.getMsg());
    }

    @Test
    public void case4() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(10000L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.5200));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(0.0420));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(3.4340));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(0));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(16.2340));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("FoodId不存在", responseResult.getMsg());
    }

    @Test
    public void case5() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        try {
            Field field = recordDetailDTO.getClass().getDeclaredField("foodId");
            field.setAccessible(true);
            field.set(recordDetailDTO, "dsdcd");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        recordDetailDTO.setFoodId("dsdcd");
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.5200));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(0.0420));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(3.4340));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(0));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(16.2340));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("FoodId不能为空", responseResult.getMsg());
    }

    @Test
    public void case6() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(null);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.5200));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(0.0420));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(3.4340));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(0));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(16.2340));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("FoodId不能为空", responseResult.getMsg());
    }

    @Test
    public void case7() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(-0.001));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.8140));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(2.9160));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(4.4420));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(3.2569));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(471.3560));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("营养素或食物质量不得小于0", responseResult.getMsg());
    }

    @Test
    public void case8() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(0.0));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.8140));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(2.9160));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(4.4420));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(3.2569));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(471.3560));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("食物质量不得小于营养素质量之和", responseResult.getMsg());
    }

    @Test
    public void case9() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(0.0));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.0));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(0.0));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(0.0));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(0.0));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(0.0));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("Add OK", responseResult.getMsg());
    }

    @Test
    public void case10() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(2000.0));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.8140));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(2.9160));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(4.4420));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(3.2569));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(471.3560));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("Add OK", responseResult.getMsg());
    }

    @Test
    public void case11() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(2000.001));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.8140));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(2.9160));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(4.4420));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(3.2569));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(471.3560));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("营养素或食物质量不得大于2000", responseResult.getMsg());
    }

    @Test
    public void case12() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);

        try {
            Field field = recordDetailDTO.getClass().getDeclaredField("foodMass");
            field.setAccessible(true);
            field.set(recordDetailDTO, "dfs");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        recordDetailDTO.setFoodMass(new BigDecimal("dfs"));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.8140));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(2.9160));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(4.4420));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(3.2569));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(471.3560));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("字段类型错误", responseResult.getMsg());
    }
    @Test
    public void case13() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(-0.01));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(2.9160));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(4.4420));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(3.2569));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(471.3560));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("营养素或食物质量不得小于0", responseResult.getMsg());
    }

    @Test
    public void case14() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(90000));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(2.9160));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(4.4420));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(3.2569));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(471.3560));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("营养素或食物质量不得大于2000", responseResult.getMsg());
    }

    @Test
    public void case15() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));

        try {
            Field field = recordDetailDTO.getClass().getDeclaredField("carbohydrateMass");
            field.setAccessible(true);
            field.set(recordDetailDTO, "ade");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        recordDetailDTO.setCarbohydrateMass(new BigDecimal("ade"));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(2.9160));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(4.4420));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(3.2569));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(471.3560));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("字段类型错误", responseResult.getMsg());
    }

    @Test
    public void case16() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.8140));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(-0.01));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(4.4420));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(3.2569));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(471.3560));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("营养素或食物质量不得小于0", responseResult.getMsg());
    }

    @Test
    public void case17() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.8140));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(90000));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(4.4420));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(3.2569));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(471.3560));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("营养素或食物质量不得大于2000", responseResult.getMsg());
    }

    @Test
    public void case18() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.8140));

        try {
            Field field = recordDetailDTO.getClass().getDeclaredField("fatMass");
            field.setAccessible(true);
            field.set(recordDetailDTO, "ade");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        recordDetailDTO.setFatMass(new BigDecimal("ade"));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(4.4420));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(3.2569));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(471.3560));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("字段类型错误", responseResult.getMsg());
    }

    @Test
    public void case19() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.8140));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(2.9160));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(-0.01));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(3.2569));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(471.3560));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("营养素或食物质量不得小于0", responseResult.getMsg());
    }

    @Test
    public void case20() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.8140));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(2.9160));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(90000));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(3.2569));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(471.3560));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("营养素或食物质量不得大于2000", responseResult.getMsg());
    }

    @Test
    public void case21() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.8140));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(2.9160));

        try {
            Field field = recordDetailDTO.getClass().getDeclaredField("proteinMass");
            field.setAccessible(true);
            field.set(recordDetailDTO, "ade");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        recordDetailDTO.setProteinMass(new BigDecimal("ade"));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(3.2569));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(471.3560));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("字段类型错误", responseResult.getMsg());
    }

    @Test
    public void case22() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.8140));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(2.9160));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(4.4420));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(-0.01));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(471.3560));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("营养素或食物质量不得小于0", responseResult.getMsg());
    }

    @Test
    public void case23() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.8140));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(2.9160));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(4.4420));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(90000));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(471.3560));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("营养素或食物质量不得大于2000", responseResult.getMsg());
    }

    @Test
    public void case24() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.8140));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(2.9160));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(4.4420));

        try {
            Field field = recordDetailDTO.getClass().getDeclaredField("celluloseMass");
            field.setAccessible(true);
            field.set(recordDetailDTO, "ade");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        recordDetailDTO.setCelluloseMass(new BigDecimal("ade"));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(471.3560));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("字段类型错误", responseResult.getMsg());
    }

    @Test
    public void case25() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.5200));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(0.0420));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(3.4340));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(3.2569));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(-0.01));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("营养素或食物质量不得小于0", responseResult.getMsg());
    }

    @Test
    public void case26() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.5200));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(0.0420));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(3.4340));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(3.2569));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(90000));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("营养素或食物质量不得大于2000", responseResult.getMsg());
    }

    @Test
    public void case27() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.5200));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(0.0420));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(3.4340));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(3.2569));

        try {
            Field field = recordDetailDTO.getClass().getDeclaredField("calorieMass");
            field.setAccessible(true);
            field.set(recordDetailDTO, "ade");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        recordDetailDTO.setCalorieMass(new BigDecimal("ade"));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("字段类型错误", responseResult.getMsg());
    }

    @Test
    public void case28() {
        RecordDetailAddDTO recordDetailDTO = new RecordDetailAddDTO();
        recordDetailDTO.setRecordId(150L);
        recordDetailDTO.setFoodId(8L);
        recordDetailDTO.setFoodMass(BigDecimal.valueOf(20.8130));
        recordDetailDTO.setCarbohydrateMass(BigDecimal.valueOf(0.5200));
        recordDetailDTO.setFatMass(BigDecimal.valueOf(0.0420));
        recordDetailDTO.setProteinMass(BigDecimal.valueOf(3.4340));
        recordDetailDTO.setCelluloseMass(BigDecimal.valueOf(0));
        recordDetailDTO.setCalorieMass(BigDecimal.valueOf(16.2340));
        ResponseResult responseResult = recordDetailService.addRecordDetail(recordDetailDTO);
        assertEquals("Add OK", responseResult.getMsg());
    }

}
