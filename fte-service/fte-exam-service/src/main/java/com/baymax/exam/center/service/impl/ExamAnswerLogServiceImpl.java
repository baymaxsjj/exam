package com.baymax.exam.center.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baymax.exam.center.enums.ExamAnswerLogEnum;
import com.baymax.exam.center.model.ExamAnswerLog;
import com.baymax.exam.center.mapper.ExamAnswerLogMapper;
import com.baymax.exam.center.model.ExamInfo;
import com.baymax.exam.center.service.IExamAnswerLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baymax.exam.common.core.enums.ClientIdEnum;
import com.baymax.exam.message.MessageResult;
import com.baymax.exam.message.enums.MessageCodeEnum;
import com.baymax.exam.message.feign.MessageServiceClient;
import com.baymax.exam.message.model.MessageInfo;
import com.baymax.exam.user.feign.ClassesClient;
import com.baymax.exam.user.feign.CourseClient;
import com.baymax.exam.user.feign.UserClient;
import com.baymax.exam.user.model.JoinClass;
import com.baymax.exam.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 考试作答日志 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-11-04
 */
@Service
public class ExamAnswerLogServiceImpl extends ServiceImpl<ExamAnswerLogMapper, ExamAnswerLog> implements IExamAnswerLogService {
    @Lazy
    @Autowired
    MessageServiceClient messageServiceClient;
    @Autowired
    UserClient userClient;
    @Autowired
    ClassesClient classesClient;

    @Async
    @Override
    public void writeLog(Integer stuId,Integer classId, ExamInfo examInfo, ExamAnswerLogEnum logEnum, String info) {
        if(classId==null){
            classId=classesClient.getClassByUserId(examInfo.getCourseId(),stuId).getId();
        }
        ExamAnswerLog answerLog=new ExamAnswerLog();
        answerLog.setStatus(logEnum);
        answerLog.setStudentId(stuId);
        answerLog.setInfo(info);
        answerLog.setExamId(examInfo.getId());
        answerLog.setClassId(classId);
        save(answerLog);
        MessageResult messageResult=new MessageResult();
        messageResult.setCode(MessageCodeEnum.EXAM_CONSOLE_STATISTICS);
        messageResult.setData(answerLog);
        MessageInfo messageInfo = new MessageInfo(examInfo.getTeacherId(), ClientIdEnum.PORTAL_CLIENT_ID);
        User userInfo = userClient.getBaseUserInfoById(stuId);
        messageInfo.setUser(userInfo.getBaseInfo());
        messageResult.setInfo(messageInfo);
        messageServiceClient.sendMessage(messageResult);
    }
    public void saveReviewStatus(int examId,int userId, ExamAnswerLogEnum status){
        LambdaQueryWrapper<ExamAnswerLog> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ExamAnswerLog::getExamId,examId);
        queryWrapper.eq(ExamAnswerLog::getStudentId,userId);
        queryWrapper.in(ExamAnswerLog::getStatus,List.of(ExamAnswerLogEnum.ROBOT_REVIEW,ExamAnswerLogEnum.TEACHER_REVIEW));
        queryWrapper.last("LIMIT 1");
        ExamAnswerLog reviewLog = getOne(queryWrapper);
        if(reviewLog==null){
            reviewLog=new ExamAnswerLog();
            reviewLog.setExamId(examId);
            reviewLog.setStudentId(userId);
        }
        reviewLog.setStatus(status);
        reviewLog.setUpdatedAt(null);
        saveOrUpdate(reviewLog);
    }
    public LambdaQueryWrapper<ExamAnswerLog> baseQueryWrapper(int examId,Integer userId,Integer classId,ExamAnswerLogEnum answerLogEnum){
        LambdaQueryWrapper<ExamAnswerLog> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ExamAnswerLog::getExamId,examId);
        if(userId!=null){
            queryWrapper.eq(ExamAnswerLog::getStudentId,userId);
        }
        if(classId!=null){
            queryWrapper.eq(ExamAnswerLog::getClassId,classId);
        }
        if(answerLogEnum!=null){
            queryWrapper.eq(ExamAnswerLog::getStatus,answerLogEnum.getValue());
        }
        queryWrapper.orderByDesc(ExamAnswerLog::getCreatedAt);
        return queryWrapper;
    }

    public ExamAnswerLog getStudentLogOne(int examId,int userId,ExamAnswerLogEnum status){
        LambdaQueryWrapper<ExamAnswerLog> queryWrapper=baseQueryWrapper(examId,userId,null,status);
        queryWrapper.last("LIMIT 1");
        return getOne(queryWrapper);
    }
    public IPage<ExamAnswerLog> getLogListByUserId(int examId,int userId,int page,int pageSize){
        final LambdaQueryWrapper<ExamAnswerLog> queryWrapper = baseQueryWrapper(examId, userId, null, null);
        return page(new Page<>(page,pageSize),queryWrapper);
    }
    public IPage<ExamAnswerLog> getLogListByClassId(int examId,int classId,int page,int pageSize){
        final LambdaQueryWrapper<ExamAnswerLog> queryWrapper = baseQueryWrapper(examId, null, classId, null);
        return page(new Page<>(page,pageSize),queryWrapper);
    }
    public IPage<ExamAnswerLog> getDistinctLogList(int examId,ExamAnswerLogEnum answerLogEnum,int page,int pageSize){
        IPage<ExamAnswerLog> iPage=new Page<>(page,pageSize);
        QueryWrapper<ExamAnswerLog> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("exam_id",examId);
        if(answerLogEnum!=null){
            queryWrapper.eq("status",answerLogEnum.getValue());
        }
        queryWrapper.select("distinct student_id");
        return page(iPage,queryWrapper);
    }
    public List<ExamAnswerLog> getLogListByExamId(int examId){
        final LambdaQueryWrapper<ExamAnswerLog> queryWrapper = baseQueryWrapper(examId, null, null, null);
        return list(queryWrapper);
    }
    public List<ExamAnswerLog> getLogListByAction(int examId,ExamAnswerLogEnum action){
        final LambdaQueryWrapper<ExamAnswerLog> queryWrapper = baseQueryWrapper(examId, null, null, action);
        return list(queryWrapper);
    }

    public List<ExamAnswerLog> getNormalLogList(int examId,int userId){
        LambdaQueryWrapper<ExamAnswerLog> queryWrapper=baseQueryWrapper(examId,userId,null,null);
        queryWrapper.orderByDesc(ExamAnswerLog::getStatus);
        queryWrapper.orderByAsc(ExamAnswerLog::getCreatedAt);
        List<ExamAnswerLogEnum> normalList=List.of(ExamAnswerLogEnum.SUBMIT,ExamAnswerLogEnum.START,ExamAnswerLogEnum.ROBOT_REVIEW,ExamAnswerLogEnum.TEACHER_REVIEW);
        queryWrapper.in(ExamAnswerLog::getStatus,normalList);
        return list(queryWrapper);
    }
}
