package com.baymax.exam.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baymax.exam.VueRouteLocationRaw;
import com.baymax.exam.common.core.base.LoginUser;
import com.baymax.exam.common.core.base.SecurityConstants;
import com.baymax.exam.common.core.exception.ResultException;
import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.message.MessageResult;
import com.baymax.exam.message.enums.MessageCodeEnum;
import com.baymax.exam.message.enums.MessageTypeEnum;
import com.baymax.exam.message.interceptor.MyWebSocketHandler;
import com.baymax.exam.message.model.MessageInfo;
import com.baymax.exam.message.service.impl.MessageInfoServiceImpl;
import com.baymax.exam.message.service.impl.MessageWebSocketService;
import com.baymax.exam.user.feign.ClassesClient;
import com.baymax.exam.user.feign.CourseClient;
import com.baymax.exam.user.feign.JoinClassClient;
import com.baymax.exam.user.feign.UserClient;
import com.baymax.exam.user.model.Courses;
import com.baymax.exam.user.model.User;
import com.baymax.exam.user.model.UserAuthInfo;
import com.baymax.exam.web.annotation.Inner;
import com.baymax.exam.web.utils.UserAuthUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-11-21
 */
@Slf4j
@Tag(name = "消息服务")
@Validated
@RestController
@RequestMapping("/message-info")
public class MessageInfoController {
    @Autowired
    MyWebSocketHandler myWebSocketHandler;
//    MessageWebSocketService messageWebSocketService;
    @Autowired
    MessageInfoServiceImpl messageInfoService;
    @Autowired
    ClassesClient classesClient;
    @Autowired
    UserClient userClient;
    @Autowired
    JoinClassClient joinClassClient;
    @Autowired
    CourseClient courseClient;

    @GetMapping("/classroom/{classId}")
    public Result getClassroomMessageList(
                                @RequestParam(required = false) Integer startId,
                                 @RequestParam(required = false, defaultValue = "1") Integer page,
                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                 @PathVariable Integer classId) throws ResultException {
        //是否在该班级
        Result info = classesClient.classInfo(classId.toString());
        Object date = info.getResultDate();
        IPage<MessageInfo> messageList = messageInfoService.getMessageList(3, classId,startId, page, pageSize);
        if(messageList.getTotal()==0){
            return Result.success(PageResult.setResult(messageList));
        }
        Set<Integer> ids=messageList.getRecords().stream().map(MessageInfo::getUserId).collect(Collectors.toSet());
        //获取用户消息
        List<User> batchUser = userClient.getBatchUser(ids);
        batchUser.forEach(user -> {
            messageList.getRecords().stream().filter(messageInfo -> Objects.equals(messageInfo.getUserId(), user.getId())).forEach(messageInfo -> {
                messageInfo.setUser(user);
            });
        });
        return Result.success(PageResult.setResult(messageList));
    }
    @PostMapping("/send/classroom/{classId}")
     public Result sendClassroomMessage(@RequestBody MessageInfo messageInfo, @PathVariable Integer classId) throws ResultException, JsonProcessingException {
        //获取班级用户id
        final Result<PageResult<UserAuthInfo>> list = joinClassClient.getList(classId, 1l, (long) SecurityConstants.CLASS_MAX_SIZE);
        PageResult<UserAuthInfo> resultDate = list.getResultDate();
        Integer userId = UserAuthUtil.getUserId();
        messageInfo.setUserId(userId);
        messageInfo.setTargetId(classId);
        messageInfo.setTitle(null);

        messageInfo.setClientId(UserAuthUtil.getUser().getClientId());
        //先这样吧
        messageInfo.setType(MessageTypeEnum.COURSE_USER_MESSAGE);
        //异步发通知

        Set<Integer> ids=resultDate.getList().stream().map(UserAuthInfo::getUserId).collect(Collectors.toSet());
        //也要通知老师
        Result<Courses> coursesResult = courseClient.getCourseByClassId(classId);
        VueRouteLocationRaw vueRouteLocationRaw = new VueRouteLocationRaw();
        try{
            if(coursesResult.getResultDate()!=null){
                vueRouteLocationRaw.setParams(Map.of("courseId",coursesResult.getResultDate().getId()));
                ids.add(coursesResult.getResultDate().getUserId());
            }
        }catch (ResultException ignored){
            log.info("老师不用发送");
        }
        vueRouteLocationRaw.setName("Classroom");
        messageInfo.setPath(vueRouteLocationRaw.getJson());
        messageInfoService.save(messageInfo);
        //不需要通知自己
        ids.remove(userId);
        User userInfo= userClient.getBaseUserInfo().getResultDate();
        messageInfo.setUser(userInfo.getBaseInfo());
        MessageResult messageResult=new MessageResult();
        messageResult.setInfo(messageInfo);
        messageResult.setCode(MessageCodeEnum.COURSE_CLASSROOM_MESSAGE);
        myWebSocketHandler.batchMessage(ids,messageResult);
        return Result.success();
     }
    @Inner
    @Operation(summary = "发送消息")
    @PostMapping("/inner/send-message")
    public Boolean sendMessage(@RequestBody @Validated MessageResult message){
        final MessageTypeEnum type = message.getInfo().getType();
        if(type!=null){
            //不是发送数据的，保存消息
            if(type.getValue()>200){
                messageInfoService.save(message.getInfo());
            }
            //系统消息全局发送
            if(type.getValue()>200&&type.getValue()<300){
                myWebSocketHandler.batchMessage(message);
                return true;
            }
        }
        return myWebSocketHandler.sendMessage(message);
    }
    @Inner
    @Operation(summary = "发送批量消息")
    @PostMapping("/inner/batch/send-message")
    public Map<Integer, Boolean> sendBatchMessage(@RequestBody @Validated MessageResult message, @RequestBody Set<Integer> ids) throws JsonProcessingException {
        return myWebSocketHandler.batchMessage(ids,message);
    }

}
