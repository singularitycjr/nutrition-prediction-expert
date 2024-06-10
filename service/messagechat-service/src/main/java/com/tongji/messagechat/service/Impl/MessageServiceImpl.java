package com.tongji.messagechat.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tongji.global.constrants.Constrants;
import com.tongji.global.enums.RoleEnum;
import com.tongji.global.util.SaTokenUtil;
import com.tongji.model.dto.websocket.MessageDTO;
import com.tongji.model.pojo.Chat;
import com.tongji.model.pojo.Doctor;
import com.tongji.model.pojo.Message;
import com.tongji.model.pojo.User;
import com.tongji.model.query.MessageQuery;
import com.tongji.model.vo.PageVO;
import com.tongji.model.vo.ResponseResult;
import com.tongji.messagechat.mapper.DoctorMapper;
import com.tongji.messagechat.mapper.MessageMapper;
import com.tongji.messagechat.mapper.UserMapper;
import com.tongji.messagechat.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    DoctorMapper doctorMapper;

    @Override
    public ResponseResult getHistory(MessageQuery messageQuery)
    {
        Integer pageNo = messageQuery.getPageNo();
        Integer pageSize = messageQuery.getPageSize();
//        Long doctorId = SaTokenUtil.getId();
        if (pageNo == null || pageNo < 1) {
            pageNo = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 5;
        }
        String orderBy = messageQuery.getOrderBy();
        Boolean isAsc = messageQuery.getIsAsc();
        if (StrUtil.isBlank(orderBy)) {
            orderBy = "time";
        }
        if (isAsc == null) {
            isAsc = false;//时间倒序
        }

        Page<Message> page = new Page<>(pageNo, pageSize);
        page.addOrder(new OrderItem(orderBy, isAsc));

        Long id = SaTokenUtil.getId();
        String role=SaTokenUtil.getRole();
        page = lambdaQuery()
                .eq(Message::getFromUserId,id)
                .eq(Message::getFromUserRole,role)
                .or(wrapper -> wrapper
                        .eq(Message::getToUserId, id)
                        .eq(Message::getToUserRole, role)
                )
                .page(page);

        List<Message> messageList=page.getRecords();
        List<MessageDTO> messageDTOList=new ArrayList<>();
        for(Message message:messageList){
            MessageDTO messageDTO=new MessageDTO();
            User patient;
            Doctor doctor;
            if(Objects.equals(message.getFromUserRole(), RoleEnum.PATIENT.getName())){
                patient=userMapper.selectById(message.getFromUserId());
                doctor=doctorMapper.selectById(message.getToUserId());
                messageDTO.setId(message.getId());
                messageDTO.setMessage(message.getMessage());
                messageDTO.setTime(message.getTime());
                messageDTO.setFromUserId(message.getFromUserId());
                messageDTO.setFromUserRole(message.getFromUserRole());
                messageDTO.setFromUserName(patient.getName());
                messageDTO.setToUserId(message.getToUserId());
                messageDTO.setToUserRole(message.getToUserRole());
                messageDTO.setToUserName(doctor.getName());
                messageDTO.setConfirmed(message.getConfirmed());
            }
            else if(Objects.equals(message.getFromUserRole(), RoleEnum.DOCTOR.getName())){
                doctor=doctorMapper.selectById(message.getFromUserId());
                patient=userMapper.selectById(message.getToUserId());
                messageDTO.setId(message.getId());
                messageDTO.setMessage(message.getMessage());
                messageDTO.setTime(message.getTime());
                messageDTO.setFromUserId(message.getFromUserId());
                messageDTO.setFromUserRole(message.getFromUserRole());
                messageDTO.setFromUserName(doctor.getName());
                messageDTO.setToUserId(message.getToUserId());
                messageDTO.setToUserRole(message.getToUserRole());
                messageDTO.setToUserName(patient.getName());
                messageDTO.setConfirmed(message.getConfirmed());
            }
            messageDTOList.add(messageDTO);

        }

        PageVO<MessageDTO> pageVO = new PageVO<>();
        pageVO.setTotal(page.getTotal());
        pageVO.setPages(page.getPages());
        pageVO.setList(messageDTOList);
        return ResponseResult.okResult(pageVO);
    }

    @Override
    public ResponseResult getLatestUnread( )
    {
        Long id = SaTokenUtil.getId();
        String role=SaTokenUtil.getRole();
        List<Message> messageList=this.list(
                Wrappers.<Message>lambdaQuery()
                        .eq(Message::getToUserId, id)
                        .eq(Message::getToUserRole, role)
                        .eq(Message::getConfirmed,false)
                        .orderByDesc(Message::getTime)
                        .last("LIMIT " + Constrants.MESSAGE_GET_BATCH_SIZE)
        );
        messageList = messageList.stream()
                .sorted(Comparator.comparing(Message::getTime)) // 按时间正序排序
                .collect(Collectors.toList());

        List<MessageDTO> messageDTOList=new ArrayList<>();
        for(Message message:messageList){
            MessageDTO messageDTO=new MessageDTO();
            User patient;
            Doctor doctor;
            if(Objects.equals(message.getFromUserRole(), RoleEnum.PATIENT.getName())){
                patient=userMapper.selectById(message.getFromUserId());
                doctor=doctorMapper.selectById(message.getToUserId());
                messageDTO.setId(message.getId());
                messageDTO.setMessage(message.getMessage());
                messageDTO.setTime(message.getTime());
                messageDTO.setFromUserId(message.getFromUserId());
                messageDTO.setFromUserRole(message.getFromUserRole());
                messageDTO.setFromUserName(patient.getName());//可以获取发信人姓名
                messageDTO.setToUserId(message.getToUserId());
                messageDTO.setToUserRole(message.getToUserRole());
                messageDTO.setToUserName(doctor.getName());//可以获取收信人姓名
                messageDTO.setConfirmed(message.getConfirmed());
            }
            else if(Objects.equals(message.getFromUserRole(), RoleEnum.DOCTOR.getName())){
                doctor=doctorMapper.selectById(message.getFromUserId());
                patient=userMapper.selectById(message.getToUserId());
                messageDTO.setId(message.getId());
                messageDTO.setMessage(message.getMessage());
                messageDTO.setTime(message.getTime());
                messageDTO.setFromUserId(message.getFromUserId());
                messageDTO.setFromUserRole(message.getFromUserRole());
                messageDTO.setFromUserName(doctor.getName());//可以获取发信人姓名
                messageDTO.setToUserId(message.getToUserId());
                messageDTO.setToUserRole(message.getToUserRole());
                messageDTO.setToUserName(patient.getName());//可以获取收信人姓名
                messageDTO.setConfirmed(message.getConfirmed());
            }
            messageDTOList.add(messageDTO);

        }

        return ResponseResult.okResult(messageDTOList);
    }
    @Override
    public ResponseResult getHistoryUnread(MessageQuery messageQuery)
    {
        Integer pageNo = messageQuery.getPageNo();
        Integer pageSize = messageQuery.getPageSize();

        if (pageNo == null || pageNo < 1) {
            pageNo = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 5;
        }
        String orderBy = messageQuery.getOrderBy();
        Boolean isAsc = messageQuery.getIsAsc();
        if (StrUtil.isBlank(orderBy)) {
            orderBy = "time";
        }
        if (isAsc == null) {
            isAsc = false;//时间倒序
        }

        Page<Message> page = new Page<>(pageNo, pageSize);
        page.addOrder(new OrderItem(orderBy, isAsc));

        Long id = SaTokenUtil.getId();
        String role=SaTokenUtil.getRole();
        page = lambdaQuery()
                .eq(Message::getToUserId, id)
                .eq(Message::getToUserRole, role)
                .eq(Message::getConfirmed,false)
                .page(page);

        List<Message> messageList=page.getRecords();
        List<MessageDTO> messageDTOList=new ArrayList<>();
        for(Message message:messageList){
            MessageDTO messageDTO=new MessageDTO();
            User patient;
            Doctor doctor;
            if(Objects.equals(message.getFromUserRole(), RoleEnum.PATIENT.getName())){
                patient=userMapper.selectById(message.getFromUserId());
                doctor=doctorMapper.selectById(message.getToUserId());
                messageDTO.setId(message.getId());
                messageDTO.setMessage(message.getMessage());
                messageDTO.setTime(message.getTime());
                messageDTO.setFromUserId(message.getFromUserId());
                messageDTO.setFromUserRole(message.getFromUserRole());
                messageDTO.setFromUserName(patient.getName());//可以获取发信人姓名
                messageDTO.setToUserId(message.getToUserId());
                messageDTO.setToUserRole(message.getToUserRole());
                messageDTO.setToUserName(doctor.getName());//可以获取收信人姓名
                messageDTO.setConfirmed(message.getConfirmed());
            }
            else if(Objects.equals(message.getFromUserRole(), RoleEnum.DOCTOR.getName())){
                doctor=doctorMapper.selectById(message.getFromUserId());
                patient=userMapper.selectById(message.getToUserId());
                messageDTO.setId(message.getId());
                messageDTO.setMessage(message.getMessage());
                messageDTO.setTime(message.getTime());
                messageDTO.setFromUserId(message.getFromUserId());
                messageDTO.setFromUserRole(message.getFromUserRole());
                messageDTO.setFromUserName(doctor.getName());//可以获取发信人姓名
                messageDTO.setToUserId(message.getToUserId());
                messageDTO.setToUserRole(message.getToUserRole());
                messageDTO.setToUserName(patient.getName());//可以获取收信人姓名
                messageDTO.setConfirmed(message.getConfirmed());
            }
            messageDTOList.add(messageDTO);

        }

        PageVO<MessageDTO> pageVO = new PageVO<>();
        pageVO.setTotal(page.getTotal());
        pageVO.setPages(page.getPages());
        pageVO.setList(messageDTOList);
        return ResponseResult.okResult(pageVO);
    }


    public ResponseResult getUnreadNum(){
        Long id = SaTokenUtil.getId();
        List<Message> messageList = this.list(
                Wrappers.<Message>lambdaQuery()
                        .eq(Message::getToUserId, id)
                        .eq(Message::getConfirmed,false)
        );
        return ResponseResult.okResult(messageList.size());
    }


    public ResponseResult getLatestSend(){
        Long id = SaTokenUtil.getId();
        String role=SaTokenUtil.getRole();
        List<Message> messageList=this.list(
                Wrappers.<Message>lambdaQuery()
                        .eq(Message::getFromUserId,id)
                        .eq(Message::getFromUserRole,role)
                        .eq(Message::getConfirmed,false)
                        .orderByDesc(Message::getTime)
                        .last("LIMIT " + Constrants.MESSAGE_GET_BATCH_SIZE)
        );
        messageList = messageList.stream()
                .sorted(Comparator.comparing(Message::getTime)) // 按时间正序排序
                .collect(Collectors.toList());

        List<MessageDTO> messageDTOList=new ArrayList<>();
        for(Message message:messageList){
            MessageDTO messageDTO=new MessageDTO();
            User patient;
            Doctor doctor;
            if(Objects.equals(message.getFromUserRole(), RoleEnum.PATIENT.getName())){
                patient=userMapper.selectById(message.getFromUserId());
                doctor=doctorMapper.selectById(message.getToUserId());
                messageDTO.setId(message.getId());
                messageDTO.setMessage(message.getMessage());
                messageDTO.setTime(message.getTime());
                messageDTO.setFromUserId(message.getFromUserId());
                messageDTO.setFromUserRole(message.getFromUserRole());
                messageDTO.setFromUserName(patient.getName());//可以获取发信人姓名
                messageDTO.setToUserId(message.getToUserId());
                messageDTO.setToUserRole(message.getToUserRole());
                messageDTO.setToUserName(doctor.getName());//可以获取收信人姓名
                messageDTO.setConfirmed(message.getConfirmed());
            }
            else if(Objects.equals(message.getFromUserRole(), RoleEnum.DOCTOR.getName())){
                doctor=doctorMapper.selectById(message.getFromUserId());
                patient=userMapper.selectById(message.getToUserId());
                messageDTO.setId(message.getId());
                messageDTO.setMessage(message.getMessage());
                messageDTO.setTime(message.getTime());
                messageDTO.setFromUserId(message.getFromUserId());
                messageDTO.setFromUserRole(message.getFromUserRole());
                messageDTO.setFromUserName(doctor.getName());//可以获取发信人姓名
                messageDTO.setToUserId(message.getToUserId());
                messageDTO.setToUserRole(message.getToUserRole());
                messageDTO.setToUserName(patient.getName());//可以获取收信人姓名
                messageDTO.setConfirmed(message.getConfirmed());
            }
            messageDTOList.add(messageDTO);

        }

        return ResponseResult.okResult(messageDTOList);
    }

    public ResponseResult getHistorySend(MessageQuery messageQuery){
        Integer pageNo = messageQuery.getPageNo();
        Integer pageSize = messageQuery.getPageSize();

        if (pageNo == null || pageNo < 1) {
            pageNo = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 5;
        }
        String orderBy = messageQuery.getOrderBy();
        Boolean isAsc = messageQuery.getIsAsc();
        if (StrUtil.isBlank(orderBy)) {
            orderBy = "time";
        }
        if (isAsc == null) {
            isAsc = false;//时间倒序
        }

        Page<Message> page = new Page<>(pageNo, pageSize);
        page.addOrder(new OrderItem(orderBy, isAsc));

        Long id = SaTokenUtil.getId();
        String role=SaTokenUtil.getRole();
        page = lambdaQuery()
                .eq(Message::getFromUserId,id)
                .eq(Message::getFromUserRole,role)
                .eq(Message::getConfirmed,false)
                .page(page);

        List<Message> messageList=page.getRecords();
        List<MessageDTO> messageDTOList=new ArrayList<>();
        for(Message message:messageList){
            MessageDTO messageDTO=new MessageDTO();
            User patient;
            Doctor doctor;
            if(Objects.equals(message.getFromUserRole(), RoleEnum.PATIENT.getName())){
                patient=userMapper.selectById(message.getFromUserId());
                doctor=doctorMapper.selectById(message.getToUserId());
                messageDTO.setId(message.getId());
                messageDTO.setMessage(message.getMessage());
                messageDTO.setTime(message.getTime());
                messageDTO.setFromUserId(message.getFromUserId());
                messageDTO.setFromUserRole(message.getFromUserRole());
                messageDTO.setFromUserName(patient.getName());//可以获取发信人姓名
                messageDTO.setToUserId(message.getToUserId());
                messageDTO.setToUserRole(message.getToUserRole());
                messageDTO.setToUserName(doctor.getName());//可以获取收信人姓名
                messageDTO.setConfirmed(message.getConfirmed());
            }
            else if(Objects.equals(message.getFromUserRole(), RoleEnum.DOCTOR.getName())){
                doctor=doctorMapper.selectById(message.getFromUserId());
                patient=userMapper.selectById(message.getToUserId());
                messageDTO.setId(message.getId());
                messageDTO.setMessage(message.getMessage());
                messageDTO.setTime(message.getTime());
                messageDTO.setFromUserId(message.getFromUserId());
                messageDTO.setFromUserRole(message.getFromUserRole());
                messageDTO.setFromUserName(doctor.getName());//可以获取发信人姓名
                messageDTO.setToUserId(message.getToUserId());
                messageDTO.setToUserRole(message.getToUserRole());
                messageDTO.setToUserName(patient.getName());//可以获取收信人姓名
                messageDTO.setConfirmed(message.getConfirmed());
            }
            messageDTOList.add(messageDTO);

        }

        PageVO<MessageDTO> pageVO = new PageVO<>();
        pageVO.setTotal(page.getTotal());
        pageVO.setPages(page.getPages());
        pageVO.setList(messageDTOList);
        return ResponseResult.okResult(pageVO);
    }

    public ResponseResult getSendNum(){
        Long id = SaTokenUtil.getId();
        List<Message> messageList = this.list(
                Wrappers.<Message>lambdaQuery()
                        .eq(Message::getFromUserId, id)
        );

        return ResponseResult.okResult(messageList.size());
    }

    public ResponseResult confirm( List<Long> idList){
        if (idList == null) {
            return ResponseResult.errorResult(400, "idList不可为空");
        }

        Long id = SaTokenUtil.getId();
        List<Message> messageList = this.list(
                Wrappers.<Message>lambdaQuery()
                        .in(Message::getId, idList)
                        .eq(Message::getToUserId, id)
//                        .and(wrapper -> wrapper
//                                .eq(Message::getToUserId, id)
//                                .or()
//                                .eq(Message::getFromUserId, id)
//                        )
        );
        for(Message message:messageList)
            message.setConfirmed(true);
        if (idList.size() != messageList.size())
            return ResponseResult.errorResult(400, "不可确认不存在消息或已确认消息");

        this.updateBatchById(messageList);
        return ResponseResult.okResult(200, "确认成功");
    }
    public ResponseResult delete( List<Long> idList){
        if (idList == null) {
            return ResponseResult.errorResult(400, "idList不可为空");
        }

        Long id = SaTokenUtil.getId();
        List<Message> messageList = this.list(
                Wrappers.<Message>lambdaQuery()
                        .in(Message::getId, idList)
                        .and(wrapper -> wrapper
                                .eq(Message::getToUserId, id)
                                .or()
                                .eq(Message::getFromUserId, id)
                        )
        );
        if (idList.size() != messageList.size())
            return ResponseResult.errorResult(400, "不可删除不存在消息");

        this.removeBatchByIds(messageList);
        return ResponseResult.okResult(200, "删除成功");
    }
}
