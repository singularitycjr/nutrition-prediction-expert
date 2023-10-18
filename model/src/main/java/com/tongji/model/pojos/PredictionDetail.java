package com.tongji.model.pojos;

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
 * 预测信息细节
 * </p>
 *
 * @author author
 * @since 2023-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("prediction_detail")
public class PredictionDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 预测id
     */
    private Long predictionId;

    /**
     * 食物名称
     */
    private String foodName;

    /**
     * 食物英文名
     */
    private String foodEn;

    /**
     * 食物质量
     */
    private String foodMass;

    /**
     * 碳水化合物质量
     */
    private String carbohydrateMass;

    /**
     * 脂肪质量
     */
    private String fatMass;

    /**
     * 蛋白质质量
     */
    private String proteinMass;

    /**
     * 纤维素质量
     */
    private String celluloseMass;

    /**
     * 热量质量
     */
    private String calorieMass;


}
