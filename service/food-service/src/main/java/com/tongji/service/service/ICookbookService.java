package com.tongji.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tongji.model.dto.patient.CookbookDTO;
import com.tongji.model.pojo.Cookbook;
import com.tongji.model.vo.ResponseResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2023-12-18
 */
public interface ICookbookService extends IService<Cookbook> {
    public ResponseResult getCookbook(CookbookDTO cookbookDTO);
}
