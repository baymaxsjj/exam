package com.baymax.exam.center.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baymax.exam.center.model.Exam;
import com.baymax.exam.center.model.ExamQuestion;
import com.baymax.exam.center.service.impl.ExamQuestionServiceImpl;
import com.baymax.exam.center.service.impl.ExamServiceImpl;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-10-26
 */
@Validated
@Tag(name = "试卷题目管理")
@RestController
@RequestMapping("/exam-question")
public class ExamQuestionController {
    @Autowired
    ExamServiceImpl examService;

    @Autowired
    ExamQuestionServiceImpl examQuestionService;

    @Deprecated
    @Operation(summary = "批量添加试卷题目")
    @PostMapping("/{examId}/batchAdd")
    public Result batchAdd(@RequestBody Set<Integer> examQuestions, @PathVariable Integer examId){
        Exam exam = examService.getById(examId);
        Integer userId = UserAuthUtil.getUserId();
        if(exam==null||exam.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //TODO:判断：题目是不是自己的呢
        return Result.msgSuccess("添加成功");
    }
    @Deprecated
    @Operation(summary = "添加试卷题目")
    @PostMapping("/delete")
    public Result delete(@RequestBody @Validated ExamQuestion examQuestion){
        Exam exam = examService.getById(examQuestion.getExamId());
        Integer userId = UserAuthUtil.getUserId();
        if(exam==null||exam.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        LambdaQueryWrapper<ExamQuestion> queryWrapper=new LambdaQueryWrapper();
        Map<SFunction<ExamQuestion, ?>,Object> queryMap=new HashMap();
        queryMap.put(ExamQuestion::getExamId,examQuestion.getExamId());
        queryMap.put(ExamQuestion::getQuestionId,examQuestion.getQuestionId());
        queryWrapper.allEq(queryMap);
        examQuestionService.remove(queryWrapper);
        return Result.msgSuccess("删除成功");
    }
}
