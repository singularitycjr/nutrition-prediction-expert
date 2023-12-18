package com.tongji.model.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2023-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cookbook")
public class Cookbook implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String location;

    private String season;

    private String energy;

    private String proteinMass;

    private String proteinEnergyRatio;

    private String carbohydrateMass;

    private String carbohydrateEnergyRatio;

    private String fatMass;

    private String fatEnergyRatio;

    private String note;


}
