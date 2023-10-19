package com.tongji.model.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 食物信息
 * </p>
 *
 * @author author
 * @since 2023-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("food")
public class Food implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 英文名
     */
    private String en;

    /**
     * 密度(g/ml)
     */
    private String density;

    /**
     * 碳水化合物质量密度(g/g)
     */
    private String carbohydrateMassDensity;

    /**
     * 碳水化合物体积密度(g/ml)
     */
    private String carbohydrateVolumeDensity;

    /**
     * 脂肪质量密度(g/g)
     */
    private String fatMassDensity;

    /**
     * 脂肪体积密度(g/ml)
     */
    private String fatVolumeDensity;

    /**
     * 蛋白质质量密度(g/g)
     */
    private String proteinMassDensity;

    /**
     * 蛋白质体积密度(g/ml)
     */
    private String proteinVolumeDensity;

    /**
     * 纤维素质量密度(g/g)
     */
    private String celluloseMassDensity;

    /**
     * 纤维素体积密度(g/ml)
     */
    private String celluloseVolumeDensity;

    /**
     * 热量质量密度(kcal/g)
     */
    private String calorieMassDensity;

    /**
     * 热量体积密度(kcal/ml)
     */
    private String calorieVolumeDensity;


}
