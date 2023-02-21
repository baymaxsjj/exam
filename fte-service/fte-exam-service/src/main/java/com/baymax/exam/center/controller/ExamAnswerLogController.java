package com.baymax.exam.center.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baymax.exam.center.enums.ExamAnswerLogEnum;
import com.baymax.exam.center.model.ExamAnswerLog;
import com.baymax.exam.center.model.ExamInfo;
import com.baymax.exam.center.service.impl.ExamAnswerLogServiceImpl;
import com.baymax.exam.center.service.impl.ExamInfoServiceImpl;
import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 考试作答日志 前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-11-04
 */
@Slf4j
@RestController
@RequestMapping("/exam-answer-log")
public class ExamAnswerLogController {
    @Autowired
    ExamInfoServiceImpl examInfoService;
    @Autowired
    ExamAnswerLogServiceImpl examAnswerLogService;

    @Operation(description = "获取作答日志统计数据")
    @GetMapping("/statistics/{examInfoId}")
    public Result statistics(@PathVariable Integer examInfoId){
        final Integer userId = UserAuthUtil.getUserId();
        final ExamInfo examInfo = examInfoService.getById(examInfoId);
        log.info("日志统计",examInfo);
        if(examInfo==null|| examInfo.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        final List<ExamAnswerLog> list = examAnswerLogService.getLogListByExamId(examInfoId);
        Map<ExamAnswerLogEnum, Set<Integer>> normalAction=new HashMap<>();
        Map<ExamAnswerLogEnum,Integer> abnormalAction=new HashMap<>();
        AtomicInteger abnormalCount= new AtomicInteger();
        list.stream().forEach(i->{
            //正常行为,去重
            ExamAnswerLogEnum status = i.getStatus();
            if(status.getValue()<20){
                Set<Integer>  userIds= normalAction.get(status);
                if(userIds==null){
                    userIds=new HashSet<>();
                }
                userIds.add(i.getStudentId());
                normalAction.put(status,userIds);
            }else if(status.getValue()>50){
                //异常行为
                Integer count = abnormalAction.get(status);
                count=(count==null)?0:count+1;
                abnormalCount.getAndIncrement();
                abnormalAction.put(status,count);
            }
        });

        Map<String,Object> result=new HashMap<>();
        result.put("abnormalCount",abnormalCount.intValue());
        result.put("normal",normalAction);
        result.put("abnormal",abnormalAction);
        return Result.success(result);
    }
    @GetMapping("/student/{examInfoId}/{studentId}")
    public Result getStudentLog(@PathVariable Integer studentId,
                                @PathVariable Integer examInfoId,
                                @RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(required = false,defaultValue = "10") Integer pageSize){
         Integer userId = UserAuthUtil.getUserId();
         ExamInfo examInfo = examInfoService.getById(examInfoId);
        if(examInfo==null|| examInfo.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        ExamAnswerLog studentLogOne = examAnswerLogService.getStudentLogOne(examInfoId, studentId,null);
        //没有日志
        if(studentLogOne==null){
            return Result.success(PageResult.setResult(new Page<>()));
        }
        //防止非法访问
        if(!Objects.equals(studentLogOne.getExamId(), examInfoId)){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        final IPage<ExamAnswerLog> logList = examAnswerLogService.getLogListByUserId(examInfoId, studentId, page, pageSize);
        return Result.success(PageResult.setResult(logList));
    }
}
