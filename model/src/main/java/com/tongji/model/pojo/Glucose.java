package com.tongji.model.pojo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author author
 * @since 2023-12-12
 */
@TableName("glucose")
public class Glucose implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private BigDecimal gluValue;

    private LocalDateTime time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public BigDecimal getGluValue() {
        return gluValue;
    }

    public void setGluValue(BigDecimal gluValue) {
        this.gluValue = gluValue;
    }
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Glucose{" +
                "id=" + id +
                ", userId=" + userId +
                ", gluValue=" + gluValue +
                ", time=" + time +
                "}";
    }
}
