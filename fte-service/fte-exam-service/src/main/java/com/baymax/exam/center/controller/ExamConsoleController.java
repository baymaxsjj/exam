package com.baymax.exam.center.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baymax.exam.center.enums.AnswerStatusEnum;
import com.baymax.exam.center.enums.ExamAnswerLogEnum;
import com.baymax.exam.center.model.ExamAnswerLog;
import com.baymax.exam.center.model.ExamInfo;
import com.baymax.exam.center.service.impl.ExamAnswerLogServiceImpl;
import com.baymax.exam.center.service.impl.ExamInfoServiceImpl;
import com.baymax.exam.center.vo.StudentActionVo;
import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.user.feign.CourseClient;
import com.baymax.exam.user.feign.JoinClassClient;
import com.baymax.exam.user.model.User;
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
    ExamAnswerLogServiceImpl examAnswerLogService;
    @Autowired
    ExamInfoServiceImpl examInfoService;

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
        courseUserPo.setStudentIds(studentIds);
        PageResult batchClassUser = joinClassClient.getBatchClassUser(courseUserPo, isIsList, page,pageSize);
        //整合行为列表
        List<User> studentList = batchClassUser.getList();
        List<StudentActionVo> studentsActionInfo = studentList.stream().map(i -> {
            StudentActionVo studentActionVo = new StudentActionVo();
            studentActionVo.setUser(i);
            //如果存在日志,就返回后10条
            boolean present = distinctLogList.stream().anyMatch(fi -> Objects.equals(fi.getStudentId(), i.getId()));
            if(present){
                 IPage<ExamAnswerLog> userLog = examAnswerLogService.getLogListByUserId(examInfoId,i.getId(),1,10);
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
//    @Operation(summary = "获取概况/监控")
//    @PostMapping("/review/list")
//    public Result getReViewList(
//            @RequestParam reviewType,
//            @RequestBody Set<Integer> classIds){
//
//    }
}
