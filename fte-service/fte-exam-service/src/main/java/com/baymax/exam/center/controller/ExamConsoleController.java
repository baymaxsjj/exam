package com.baymax.exam.center.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baymax.exam.center.enums.*;
import com.baymax.exam.center.model.*;
import com.baymax.exam.center.service.impl.*;
import com.baymax.exam.center.vo.AnswerQuestionResultVo;
import com.baymax.exam.center.vo.QuestionInfoVo;
import com.baymax.exam.center.vo.StudentActionVo;
import com.baymax.exam.center.vo.StudentReviewVo;
import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.user.feign.CourseClient;
import com.baymax.exam.user.feign.JoinClassClient;
import com.baymax.exam.user.feign.UserAuthInfoClient;
import com.baymax.exam.user.model.UserAuthInfo;
import com.baymax.exam.user.po.CourseUserPo;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/18 9:40
 * @description：考试控制台
 * @modified By：
 * @version:
 */
@Slf4j
@Validated
@Tag(name = "考试控制台")
@RestController
@RequestMapping("/exam-console/{examInfoId}")
public class ExamConsoleController {
    @Autowired
    CourseClient courseClient;
    @Autowired
    JoinClassClient joinClassClient;
    @Autowired
    UserAuthInfoClient userAuthInfoClient;
    @Autowired
    ExamAnswerLogServiceImpl examAnswerLogService;
    @Autowired
    ExamInfoServiceImpl examInfoService;
    @Autowired
    QuestionServiceImpl questionService;
    @Autowired
    ExamCenterServiceImpl examCenterService;
    @Autowired
    ExamScoreRecordServiceImpl scoreRecordService;
    @Autowired
    ExamAnswerResultServiceImpl answerResultService;

    /**
     * 获取总览
     * 答题进度、
     *
     * @param examInfoId 考试信息id
     * @return {@link Result}
     */
    @Operation(summary = "获取概况/监控")
    @PostMapping("/outline-monitor")
    public Result getOutline(
            @PathVariable Integer examInfoId,
            @RequestBody Set<Integer> classIds,
            @RequestParam AnswerStatusEnum status,
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(required = false,defaultValue = "10") Long pageSize){
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        Integer userId = UserAuthUtil.getUserId();
        if(examInfo==null||examInfo.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //TODO:判断班级是不是自己的
        Set<Integer> studentIds=null;
        boolean isIsList=true;
        // 查询行为日志
        ExamAnswerLogEnum answerLogEnum=null;
        switch (status){
            case NOT_START:
                isIsList=false;
            case START:
                answerLogEnum=ExamAnswerLogEnum.START;
                break;
            case SUBMIT:
                answerLogEnum=ExamAnswerLogEnum.SUBMIT;
                break;
        }
        List<ExamAnswerLog> distinctLogList = examAnswerLogService.getDistinctLogList(examInfoId, answerLogEnum);
        //查全部
        if(status!=AnswerStatusEnum.ALL){
            studentIds=distinctLogList.stream().map(i->i.getStudentId()).collect(Collectors.toSet());
        }
        //获取学生信息列表
        CourseUserPo courseUserPo=new CourseUserPo();
        courseUserPo.setClassIds(classIds);
        courseUserPo.setCourseId(examInfo.getCourseId());
        courseUserPo.setStudentIds(studentIds);
        PageResult batchClassUser = joinClassClient.getBatchClassUser(courseUserPo, isIsList, page,pageSize);
        //整合行为列表
        List<UserAuthInfo> studentList = batchClassUser.getList();
        List<StudentActionVo> studentsActionInfo = studentList.stream().map(studentInfo -> {
            StudentActionVo studentActionVo = new StudentActionVo();
            studentActionVo.setUserAuthInfo(studentInfo);
            //如果存在日志,就返回后10条
            boolean present = distinctLogList.stream().anyMatch(fi -> Objects.equals(fi.getStudentId(), studentInfo.getUserId()));
            if(present){
                 IPage<ExamAnswerLog> userLog = examAnswerLogService.getLogListByUserId(examInfoId,studentInfo.getUserId(),1,10);
                Collections.reverse(userLog.getRecords());
                 studentActionVo.setActionPage(PageResult.setResult(userLog));
                 userLog.getRecords().stream().filter(log->log.getStatus()==ExamAnswerLogEnum.SUBMIT).findFirst().ifPresentOrElse(log->{
                     studentActionVo.setAnswerStatus(AnswerStatusEnum.SUBMIT);
                    },()->{
                     studentActionVo.setAnswerStatus(AnswerStatusEnum.START);
                    }
                 );
            }else{
                studentActionVo.setAnswerStatus(AnswerStatusEnum.NOT_START);
            }
            return studentActionVo;
        }).collect(Collectors.toList());
        batchClassUser.setList(studentsActionInfo);
        return Result.success(batchClassUser);
    }
    @Operation(summary = "获取批阅列表")
    @PostMapping("/review/list")
    public Result getReViewList(
            @RequestParam(required = false,defaultValue = "all") String reviewType,
            @RequestBody Set<Integer> classIds,
            @PathVariable Integer examInfoId,
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(required = false,defaultValue = "10") Long pageSize){
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        Integer userId = UserAuthUtil.getUserId();
        if(examInfo==null||examInfo.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        Set<Integer> studentIds=null;
        boolean isIsList=true;
        ExamAnswerLogEnum answerLogEnum=null;
        switch (reviewType){
            case "none":
                answerLogEnum=ExamAnswerLogEnum.SUBMIT;
                isIsList=false;
                break;
            case "robot":
                answerLogEnum=ExamAnswerLogEnum.SUBMIT;
                break;
            case "teacher":
                answerLogEnum=ExamAnswerLogEnum.TEACHER_REVIEW;
                break;
        }
        if(answerLogEnum!=null){
            List<ExamAnswerLog> distinctLogList = examAnswerLogService.getDistinctLogList(examInfoId, answerLogEnum);
            studentIds=distinctLogList.stream().map(i->i.getStudentId()).collect(Collectors.toSet());
        }

        CourseUserPo courseUserPo=new CourseUserPo();
        courseUserPo.setClassIds(classIds);
        courseUserPo.setCourseId(examInfo.getCourseId());
        courseUserPo.setStudentIds(studentIds);
        //学生列表
        PageResult batchClassUser = joinClassClient.getBatchClassUser(courseUserPo, isIsList, page,pageSize);
        List<UserAuthInfo> studentList = batchClassUser.getList();
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
                    List<ExamScoreRecord> scoreList = scoreRecordService.getScoreList(examInfoId, studentInfo.getUserId());
                    examCenterService.statisticalAnswerInfo(studentReviewVo,scoreList,questionInfoVos);
                }
            }
            return studentReviewVo;
        }).collect(Collectors.toList());
        batchClassUser.setList(studentsActionInfo);
        return Result.success(batchClassUser);
    }
    @Operation(summary = "获取批阅列表")
    @GetMapping("/review/{studentId}")
    public Result getReViewAnswer(
            @PathVariable Integer examInfoId,
            @PathVariable Integer studentId) {
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        Integer userId = UserAuthUtil.getUserId();
        if (examInfo == null || !Objects.equals(examInfo.getTeacherId(), userId)) {
            //自己查看自己
            if(!(Objects.equals(studentId, userId) &&examInfo.getEndVisible())){
                return Result.failed(ResultCode.PARAM_ERROR);
            }
        }
        //获取考试题目
        List<QuestionInfoVo> questionInfoVos = questionService.examQuestionInfo(examInfo.getExamId());
        log.info("学生作答/题目信息：{}",questionInfoVos);
        //获取作答
        final List<ExamAnswerResult> answerResultList = answerResultService.getAnswerResultList(examInfoId, studentId);
       if(answerResultList.isEmpty()){
           return Result.msgWaring("未获取到作答信息");
       }
        //获取批阅
        final List<ExamScoreRecord> scoreList = scoreRecordService.getScoreList(examInfoId, studentId);
        //整合
        List<AnswerQuestionResultVo>  answerQuestionResultList = questionInfoVos.stream().map(questionInfo -> {
            AnswerQuestionResultVo answerQuestionResult = new AnswerQuestionResultVo();
            List<ExamAnswerResult> answerResults = answerResultList.stream().filter(answerResult -> Objects.equals(answerResult.getQuestionId(), questionInfo.getId())).toList();
            answerQuestionResult.setAnswerResult(answerResults);
            scoreList.stream().filter(score -> Objects.equals(score.getQuestionId(), questionInfo.getId())).findFirst().ifPresent(score -> {
                answerQuestionResult.setScoreRecord(score);
            });
            answerQuestionResult.setQuestionInfo(questionInfo);
            return answerQuestionResult;
        }).toList();
        log.info("学生作答/批阅信息：{}",answerQuestionResultList);
        //2.题目类型分类
        //题目类型排序
        Map<QuestionTypeEnum, List<AnswerQuestionResultVo>> resultGroup = answerQuestionResultList.stream().collect(Collectors.groupingBy(result -> result.getQuestionInfo().getType()));
        //题目类型排序
        TreeMap<QuestionTypeEnum, List<AnswerQuestionResultVo>> questionResultOrderGroup=new TreeMap<>(Comparator.comparing(QuestionTypeEnum::getValue));
        questionResultOrderGroup.putAll(resultGroup);
        log.info("学生作答/批阅信息：分组信息：有序{}",questionResultOrderGroup);
        StudentReviewVo studentReview=new StudentReviewVo();
        examCenterService.statisticalAnswerInfo(studentReview,scoreList,questionInfoVos);
        //防止非法获取认证信息
        UserAuthInfo userAuthInfo = userAuthInfoClient.getStudentInfo(studentId);
        studentReview.setUserAuthInfo(userAuthInfo);
        studentReview.setAnswerResults(questionResultOrderGroup);

        return Result.success(studentReview);
    }

}
