package com.baymax.exam.center.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baymax.exam.center.enums.ExamAnswerLogEnum;
import com.baymax.exam.center.model.ExamAnswerLog;
import com.baymax.exam.center.model.ExamInfo;
import com.baymax.exam.center.service.impl.ExamAnswerLogServiceImpl;
import com.baymax.exam.center.service.impl.ExamInfoServiceImpl;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

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
        final List<ExamAnswerLog> list = examAnswerLogService.getLogListByStatus(examInfoId,null);
        Map<ExamAnswerLogEnum, Set<Integer>> normalAction=new HashMap<>();
        Map<ExamAnswerLogEnum,Integer> abnormalAction=new HashMap<>();
        list.stream().forEach(i->{
            //正常行为,去重
            ExamAnswerLogEnum status = i.getStatus();
            if(status==ExamAnswerLogEnum.START||status==ExamAnswerLogEnum.SUBMIT){
                Set<Integer>  userIds= normalAction.get(status);
                if(userIds==null){
                    userIds=new HashSet<>();
                }
                userIds.add(i.getStudentId());
                normalAction.put(status,userIds);
            }else{
                Integer count = abnormalAction.get(status);
                count=(count==null)?0:count+1;
                abnormalAction.put(status,count);
            }
        });
        
        Map<String,Object> result=new HashMap<>();
        result.put("total",list.size());
        result.put("normal",normalAction);
        result.put("abnormal",abnormalAction);
        return Result.success(result);
    }
}
