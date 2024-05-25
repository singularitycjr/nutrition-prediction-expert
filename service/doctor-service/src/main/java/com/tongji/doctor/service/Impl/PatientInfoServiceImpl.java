package com.tongji.doctor.service.Impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tongji.doctor.mapper.UserDetailMapper;
import com.tongji.doctor.mapper.UserMapper;
import com.tongji.doctor.service.IPatientInfoService;
import com.tongji.global.util.SaTokenUtil;
import com.tongji.model.dto.doctor.PatientBriefDTO;
import com.tongji.model.dto.doctor.PatientDTO;
import com.tongji.model.pojo.Food;
import com.tongji.model.pojo.User;
import com.tongji.model.pojo.UserDetail;
import com.tongji.model.query.FoodQuery;
import com.tongji.model.query.PatientQuery;
import com.tongji.model.vo.PageVO;
import com.tongji.model.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class PatientInfoServiceImpl extends ServiceImpl<UserMapper, User> implements IPatientInfoService {
    @Autowired
    UserDetailMapper userDetailMapper;

    @Override
    public ResponseResult getAll(PatientQuery patientQuery) {
        Integer pageNo = patientQuery.getPageNo();
        Integer pageSize = patientQuery.getPageSize();
//        Long doctorId = SaTokenUtil.getId();
        if (pageNo == null || pageNo < 1) {
            pageNo = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 5;
        }
        String orderBy = patientQuery.getOrderBy();
        Boolean isAsc = patientQuery.getIsAsc();
        if (StrUtil.isBlank(orderBy)) {
            orderBy = "id";
        }
        if (isAsc == null) {
            isAsc = true;
        }

        Page<User> page = new Page<>(pageNo, pageSize);
        page.addOrder(new OrderItem(orderBy, isAsc));

        Long id = SaTokenUtil.getId();
        page = lambdaQuery()
                .like(StrUtil.isNotBlank(patientQuery.getName()), User::getName, "%" + patientQuery.getName() + "%")
                .like(StrUtil.isNotBlank(patientQuery.getAccount()), User::getAccount, "%" + patientQuery.getAccount() + "%")
                .page(page);

        List<User> userList =page.getRecords();
        List<PatientDTO> patientList = new ArrayList<>();

        for (User user : userList) {
            PatientDTO patientDTO = new PatientDTO();
            patientDTO.setId(user.getId());
            patientDTO.setAccount(user.getAccount());
            patientDTO.setName(user.getName());
            patientDTO.setProfile(user.getProfile());

            UserDetail userDetail = userDetailMapper.selectOne(
                    Wrappers.<UserDetail>lambdaQuery()

                            .eq(UserDetail::getUserId, user.getId())
            );
            patientDTO.setAge(userDetail.getAge());
            patientDTO.setDiabetesType(userDetail.getDiabetesType());
            patientDTO.setGender(userDetail.getGender());
            patientDTO.setHeight(userDetail.getHeight());
            patientDTO.setWeight(userDetail.getWeight());

            patientList.add(patientDTO);
        }

        PageVO<PatientDTO> pageVO = new PageVO<>();
        pageVO.setTotal(page.getTotal());
        pageVO.setPages(page.getPages());
        pageVO.setList(patientList);
        return ResponseResult.okResult(pageVO);

    }

    @Override
    public ResponseResult getById(Long id) {
        if (id == null) {
            return ResponseResult.errorResult(400, "患者id不可为空");
        }

        Long doctorId = SaTokenUtil.getId();
        User user = this.getOne(
                Wrappers.<User>lambdaQuery().
                        eq(User::getId, id)
        );
        if (user == null) {
            return ResponseResult.errorResult(400, "患者不存在");
        }
        if (user.getDoctor() == null || !Objects.equals(user.getDoctor(), doctorId)) {
            return ResponseResult.errorResult(400, "您无权限查看该患者详细信息");
        }

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(user.getId());
        patientDTO.setAccount(user.getAccount());
        patientDTO.setName(user.getName());
        patientDTO.setProfile(user.getProfile());

        UserDetail userDetail = userDetailMapper.selectOne(
                Wrappers.<UserDetail>lambdaQuery().
                        eq(UserDetail::getUserId, user.getId())
        );
        patientDTO.setAge(userDetail.getAge());
        patientDTO.setDiabetesType(userDetail.getDiabetesType());
        patientDTO.setGender(userDetail.getGender());
        patientDTO.setHeight(userDetail.getHeight());
        patientDTO.setWeight(userDetail.getWeight());


        return ResponseResult.okResult(patientDTO);
    }

    @Override
    public ResponseResult getOwnPatients(PatientQuery patientQuery) {
//        if (StrUtil.isBlank(patientQuery.getAccount()) && StrUtil.isBlank(patientQuery.getName())) {
//            return ResponseResult.errorResult(400, "查询条件不可为空");
//        }

        Integer pageNo = patientQuery.getPageNo();
        Integer pageSize = patientQuery.getPageSize();
        Long doctorId = SaTokenUtil.getId();
        if (pageNo == null || pageNo < 1) {
            pageNo = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 5;
        }
        String orderBy = patientQuery.getOrderBy();
        Boolean isAsc = patientQuery.getIsAsc();
        if (StrUtil.isBlank(orderBy)) {
            orderBy = "id";
        }
        if (isAsc == null) {
            isAsc = true;
        }

        Page<User> page = new Page<>(pageNo, pageSize);
        page.addOrder(new OrderItem(orderBy, isAsc));


        page = lambdaQuery()
                .like(StrUtil.isNotBlank(patientQuery.getName()), User::getName, "%" + patientQuery.getName() + "%")
                .like(StrUtil.isNotBlank(patientQuery.getAccount()), User::getAccount, "%" + patientQuery.getAccount() + "%")
                .eq(User::getDoctor, doctorId)
                .page(page);


        List<User> userList = page.getRecords();
        List<PatientDTO> patientList = new ArrayList<>();
        for (User user : userList) {
            PatientDTO patientDTO = new PatientDTO();
            patientDTO.setId(user.getId());
            patientDTO.setAccount(user.getAccount());
            patientDTO.setName(user.getName());
            patientDTO.setProfile(user.getProfile());

            UserDetail userDetail = userDetailMapper.selectOne(
                    Wrappers.<UserDetail>lambdaQuery()
                            .eq(UserDetail::getUserId, user.getId())
            );
            patientDTO.setAge(userDetail.getAge());
            patientDTO.setDiabetesType(userDetail.getDiabetesType());
            patientDTO.setGender(userDetail.getGender());
            patientDTO.setHeight(userDetail.getHeight());
            patientDTO.setWeight(userDetail.getWeight());

            patientList.add(patientDTO);
        }


        PageVO<PatientDTO> pageVO = new PageVO<>();
        pageVO.setTotal(page.getTotal());
        pageVO.setPages(page.getPages());
        pageVO.setList(patientList);
        return ResponseResult.okResult(pageVO);
    }
}
