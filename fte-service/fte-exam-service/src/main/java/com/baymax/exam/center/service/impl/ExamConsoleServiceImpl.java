package com.baymax.exam.center.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baymax.exam.center.enums.ExamAnswerLogEnum;
import com.baymax.exam.center.model.ExamAnswerLog;
import com.baymax.exam.center.model.ExamClass;
import com.baymax.exam.center.model.ExamInfo;
import com.baymax.exam.center.model.ExamScoreRecord;
import com.baymax.exam.center.vo.QuestionInfoVo;
import com.baymax.exam.center.vo.StudentReviewVo;
import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.user.feign.JoinClassClient;
import com.baymax.exam.user.model.UserAuthInfo;
import com.baymax.exam.user.po.CourseUserPo;
import com.baymax.exam.web.utils.UserAuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ：Baymax
 * @date ：Created in 2023/1/1 15:28
 * @description：控制台服务
 * @modified By：
 * @version:
 */
@Slf4j
@Service
public class ExamConsoleServiceImpl {
    @Autowired
    ExamAnswerLogServiceImpl examAnswerLogService;
    @Autowired
    ExamInfoServiceImpl examInfoService;
    @Autowired
    ExamClassServiceImpl examClassService;
    @Autowired
    JoinClassClient joinClassClient;
    @Autowired
    QuestionServiceImpl questionService;

    @Autowired
    ExamCenterServiceImpl examCenterService;
    @Autowired
    ExamScoreRecordServiceImpl scoreRecordService;

    public PageResult getReViewList(
            String reviewType,
            Integer classId,
            Integer examInfoId,
            Long page,
            Long pageSize){
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        Set<Integer> studentIds=null;
        boolean isIsList=true;
        ExamAnswerLogEnum answerLogEnum=null;
        switch (reviewType){
            case "none":
                answerLogEnum=ExamAnswerLogEnum.SUBMIT;
                isIsList=false;
                break;
            case "robot":
                answerLogEnum=ExamAnswerLogEnum.ROBOT_REVIEW;
                break;
            case "teacher":
                answerLogEnum=ExamAnswerLogEnum.TEACHER_REVIEW;
                break;
        }
        if(answerLogEnum!=null){
            List<ExamAnswerLog> distinctLogList = examAnswerLogService.getLogListByAction(examInfoId, answerLogEnum);
            studentIds=distinctLogList.stream().map(ExamAnswerLog::getStudentId).collect(Collectors.toSet());
        }
        final PageResult<UserAuthInfo> userInfo = getUserInfo(examInfoId, examInfo.getCourseId(), classId, studentIds, isIsList, page, pageSize);
        List<UserAuthInfo> studentList = userInfo.getList();
        //题目选项
        List<QuestionInfoVo> questionInfoVos = questionService.examQuestionInfo(examInfo.getExamId());
        List<StudentReviewVo> studentsActionInfo = studentList.stream().map(studentInfo -> {
            StudentReviewVo studentReviewVo = new StudentReviewVo();
            studentReviewVo.setUserAuthInfo(studentInfo);
            List<ExamAnswerLog> normalLogList = examAnswerLogService.getNormalLogList(examInfoId, studentInfo.getUserId());
            //如果存在日志,就返回后10条
            if(normalLogList!=null){
                normalLogList.stream().findFirst().ifPresent(answerLog->{
                    studentReviewVo.setAnswerStatus(answerLog.getStatus());
                });
                normalLogList.stream().filter(log->log.getStatus()==ExamAnswerLogEnum.START).findFirst().ifPresent(answerLog->{
                    studentReviewVo.setStartTime(answerLog.getCreatedAt());
                });
                normalLogList.stream().filter(log->log.getStatus()==ExamAnswerLogEnum.SUBMIT).findFirst().ifPresent(answerLog->{
                    studentReviewVo.setSubmitTime(answerLog.getCreatedAt());
                });
                if(studentReviewVo.getAnswerStatus()!=ExamAnswerLogEnum.START){
                    List<ExamScoreRecord> scoreList = scoreRecordService.getScoreListByUserId(examInfoId, studentInfo.getUserId());
                    examCenterService.statisticalAnswerInfo(studentReviewVo,scoreList,questionInfoVos);
                }
            }
            return studentReviewVo;
        }).collect(Collectors.toList());
        return PageResult.copyPageResult(userInfo,studentsActionInfo);
    }
    public PageResult<UserAuthInfo> getUserInfo(int examId,int courseId,Integer classId,Set<Integer> stuIds,boolean isIsList,long page,long pageSize){
        Set<Integer> ids;
        if(classId!=null){
           ids=new HashSet<>();
           ids.add(classId);
        }else{
           ids = examClassService.getExamClassIds(examId).stream().map(ExamClass::getClassId).collect(Collectors.toSet());
        }

        CourseUserPo courseUserPo=new CourseUserPo();
        courseUserPo.setClassIds(ids);
        courseUserPo.setCourseId(courseId);
        courseUserPo.setStudentIds(stuIds);
        //学生列表
        return joinClassClient.getBatchClassUser(courseUserPo, isIsList, page,pageSize);
    }


}
