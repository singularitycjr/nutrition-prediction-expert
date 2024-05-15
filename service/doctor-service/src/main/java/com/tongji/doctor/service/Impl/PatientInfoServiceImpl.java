package com.tongji.doctor.service.Impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tongji.doctor.mapper.UserDetailMapper;
import com.tongji.doctor.mapper.UserMapper;
import com.tongji.doctor.service.IPatientInfoService;
import com.tongji.global.util.SaTokenUtil;
import com.tongji.model.dto.doctor.PatientBriefDTO;
import com.tongji.model.dto.doctor.PatientDTO;
import com.tongji.model.dto.doctor.PatientQueryDTO;
import com.tongji.model.pojo.Food;
import com.tongji.model.pojo.Glucose;
import com.tongji.model.pojo.User;
import com.tongji.model.pojo.UserDetail;
import com.tongji.model.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class PatientInfoServiceImpl extends ServiceImpl<UserMapper, User> implements IPatientInfoService {
    @Autowired
    UserDetailMapper userDetailMapper;

    @Override
    public ResponseResult getAll() {
        Long id = SaTokenUtil.getId();
        List<User> userList = this.list(
                Wrappers.<User>lambdaQuery().
                        eq(User::getDoctor, id)
        );
        List<PatientDTO> patientList = new ArrayList<>();

        for (User user : userList) {
            PatientDTO patientDTO = new PatientDTO();
            patientDTO.setId(user.getId());
            patientDTO.setAccount(user.getAccount());
            patientDTO.setName(user.getName());

            UserDetail userDetail = userDetailMapper.selectOne(
                    Wrappers.<UserDetail>lambdaQuery().
                            eq(UserDetail::getUserId, user.getId())
            );
            patientDTO.setAge(userDetail.getAge());
            patientDTO.setDiabetesType(userDetail.getDiabetesType());
            patientDTO.setGender(userDetail.getGender());
            patientDTO.setHeight(userDetail.getHeight());
            patientDTO.setWeight(userDetail.getWeight());

            patientList.add(patientDTO);
        }

        return ResponseResult.okResult(patientList);
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
        if(user==null)
        {
            return ResponseResult.errorResult(400, "患者不存在");
        }
        if(user.getDoctor()==null|| !Objects.equals(user.getDoctor(), doctorId))
        {
            return ResponseResult.errorResult(400, "您无权限查看该患者详细信息");
        }

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(user.getId());
        patientDTO.setAccount(user.getAccount());
        patientDTO.setName(user.getName());

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
    public ResponseResult getByProperty(PatientQueryDTO patientQueryDTO) {
        if (StrUtil.isBlank(patientQueryDTO.getAccount()) && StrUtil.isBlank(patientQueryDTO.getName())) {
            return ResponseResult.errorResult(400, "查询条件不可为空");
        }
        List<User> userList = this.list(
                Wrappers.<User>lambdaQuery()
                        .like(StrUtil.isNotBlank(patientQueryDTO.getName()), User::getName, "%" + patientQueryDTO.getName() + "%")
                        .like(StrUtil.isNotBlank(patientQueryDTO.getAccount()), User::getAccount, "%" + patientQueryDTO.getAccount() + "%")
        );
        System.out.println(userList);

        List<PatientBriefDTO> patientBriefList = new ArrayList<>();
        for (User user : userList) {
            System.out.println(user);
            UserDetail userDetail = userDetailMapper.selectOne(
                    Wrappers.<UserDetail>lambdaQuery().
                            eq(UserDetail::getUserId, user.getId())
            );
            System.out.println(userDetail);

            PatientBriefDTO patientBriefDTO = new PatientBriefDTO();
            patientBriefDTO.setId(user.getId());
            patientBriefDTO.setGender(userDetail.getGender());
            patientBriefDTO.setAccount(user.getAccount());
            patientBriefDTO.setName(user.getName());

            patientBriefList.add(patientBriefDTO);
        }

        return ResponseResult.okResult(patientBriefList);
    }
}