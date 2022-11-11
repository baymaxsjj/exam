package com.baymax.exam.center.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baymax.exam.center.model.ExamPaper;
import com.baymax.exam.center.model.ExamQuestion;
import com.baymax.exam.center.service.impl.ExamQuestionServiceImpl;
import com.baymax.exam.center.service.impl.ExamPaperServiceImpl;
import com.baymax.exam.center.service.impl.QuestionServiceImpl;
import com.baymax.exam.center.vo.ExamPaperVo;
import com.baymax.exam.center.vo.QuestionInfoVo;
import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.user.feign.UserServiceClient;
import com.baymax.exam.user.model.Courses;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 考试试卷信息表 前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-10-26
 */
@Tag(name = "试卷管理")
@Validated
@RestController
@RequestMapping("/exam-paper")
public class ExamPaperController {
    @Autowired
    UserServiceClient userServiceClient;
    @Autowired
    ExamPaperServiceImpl examService;
    @Autowired
    ExamQuestionServiceImpl examQuestionService;
    @Autowired
    QuestionServiceImpl questionService;

    @Operation(summary = "添加/更新试卷")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated ExamPaperVo paperVo) {
        ExamPaper examPaper =paperVo.getExamPaper();
        String info = "添加成功";
        Integer teacherId = null;
        //更改，以前那种可能会查询两次数据库
        if (examPaper.getId() != null) {
            ExamPaper temExamPaper = examService.getById(examPaper.getId());
            if (temExamPaper != null) {
                teacherId = temExamPaper.getTeacherId();
            }
            info = "更新成功";
        } else {
            Courses course = userServiceClient.findCourse(examPaper.getCourseId());
            if (course != null) {
                teacherId = course.getUserId();
            }
        }
        Integer userId = UserAuthUtil.getUserId();
        if (teacherId != userId) {
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //删除试卷题目，然后在添加
        if (examPaper.getId() != null) {
            LambdaQueryWrapper<ExamQuestion> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(ExamQuestion::getExamId, examPaper.getId());
            examQuestionService.remove(queryWrapper);
        }
        examPaper.setTeacherId(userId);
        //更新试卷选项
        examService.saveOrUpdate(examPaper);
        //重新添加题目
        List<ExamQuestion> list=paperVo.getQuestions().stream().map(integer ->{
            ExamQuestion examQuestion = new ExamQuestion();
            examQuestion.setQuestionId(integer);
            examQuestion.setExamId(examPaper.getId());
            return examQuestion;
        }).collect(Collectors.toList());
        examQuestionService.saveBatch(list);
        return Result.msgSuccess(info);
    }
    @Operation(summary = "删除试卷")
    @PostMapping("/delete/{examId}")
    public Result delete(@PathVariable Integer examId){
        ExamPaper examPaper = examService.getById(examId);
        Integer userId = UserAuthUtil.getUserId();
        if(examPaper ==null|| examPaper.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        examService.removeById(examId);
        return Result.msgSuccess("删除成功");
    }
    @Operation(summary = "获取试卷题目信息")
    @GetMapping("/detail/{examId}")
    public Result detail(@PathVariable Integer examId){
        ExamPaper examPaper = examService.getById(examId);
        Integer userId = UserAuthUtil.getUserId();
        if(examPaper ==null|| examPaper.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        LambdaQueryWrapper<ExamQuestion> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ExamQuestion::getExamId,examId);
        List<ExamQuestion> list = examQuestionService.list(queryWrapper);
        ExamPaperVo paper=new ExamPaperVo();
        paper.setExamPaper(examPaper);
        paper.setQuestions(list.stream().map(item->item.getQuestionId()).collect(Collectors.toSet()));
        return Result.success(paper);
    }
    @Operation(summary = "获取试卷信息")
    @GetMapping("/info/{examId}")
    public Result info(@PathVariable Integer examId){
        ExamPaper examPaper = examService.getById(examId);
        Integer userId = UserAuthUtil.getUserId();
        if(examPaper ==null|| examPaper.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        return Result.success(examPaper);
    }
    @Operation(summary = "获取试卷题目信息")
    @GetMapping("/quesiton/{examId}")
    public Result quesiton(@PathVariable Integer examId){
        ExamPaper examPaper = examService.getById(examId);
        Integer userId = UserAuthUtil.getUserId();
        if(examPaper ==null|| examPaper.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        List<QuestionInfoVo> questionInfos = questionService.examQuestionInfo(examId);
        return Result.success(questionInfos);
    }
    @Operation(summary = "获取试卷列表")
    @GetMapping("/list/{courseId}")
    public Result list(@PathVariable Integer courseId,
                       @RequestParam(defaultValue = "1",required = false) Integer page,
                       @RequestParam(defaultValue = "10",required = false) Integer pageSize){
        Integer userId = UserAuthUtil.getUserId();
        LambdaQueryWrapper<ExamPaper> queryWrapper=new LambdaQueryWrapper();
        Map<SFunction<ExamPaper, ?>, Object> queryMap=new HashMap<>();
        queryMap.put(ExamPaper::getCourseId,courseId);
        queryMap.put(ExamPaper::getTeacherId,userId);
        queryWrapper.allEq(queryMap);
        queryWrapper.orderByDesc(ExamPaper::getCreatedAt);
        Page<ExamPaper> pa=new Page(page,pageSize);
        Page<ExamPaper> record = examService.page(pa, queryWrapper);
        return Result.success(PageResult.setResult(record));
    }
}
