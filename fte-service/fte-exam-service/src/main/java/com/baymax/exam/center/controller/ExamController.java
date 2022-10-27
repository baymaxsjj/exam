package com.baymax.exam.center.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baymax.exam.center.model.Exam;
import com.baymax.exam.center.model.ExamQuestion;
import com.baymax.exam.center.service.impl.ExamQuestionServiceImpl;
import com.baymax.exam.center.service.impl.ExamServiceImpl;
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

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    UserServiceClient userServiceClient;
    @Autowired
    ExamServiceImpl examService;
    @Autowired
    ExamQuestionServiceImpl examQuestionService;
    @Autowired
    QuestionServiceImpl questionService;

    @Operation(summary = "添加/更新试卷")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated ExamPaperVo paperVo) {
        Exam exam=paperVo.getExam();
        String info = "添加成功";
        Integer teacherId = null;
        //更改，以前那种可能会查询两次数据库
        if (exam.getId() != null) {
            Exam temExam = examService.getById(exam.getId());
            if (temExam != null) {
                teacherId = temExam.getTeacherId();
            }
            info = "更新成功";
        } else {
            Courses course = userServiceClient.findCourse(exam.getCourseId());
            if (course != null) {
                teacherId = course.getUserId();
            }
        }
        Integer userId = UserAuthUtil.getUserId();
        if (teacherId != userId) {
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //删除试卷题目，然后在添加
        if (exam.getId() != null) {
            LambdaQueryWrapper<ExamQuestion> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(ExamQuestion::getExamId,exam.getId());
            examQuestionService.remove(queryWrapper);
        }
        exam.setTeacherId(userId);
        //更新试卷选项
        examService.saveOrUpdate(exam);
        //重新添加题目
        List<ExamQuestion> list=paperVo.getQuestions().stream().map(integer ->{
            ExamQuestion examQuestion = new ExamQuestion();
            examQuestion.setQuestionId(integer);
            examQuestion.setExamId(exam.getId());
            return examQuestion;
        }).collect(Collectors.toList());
        examQuestionService.saveBatch(list);
        return Result.msgSuccess(info);
    }
    @Operation(summary = "删除试卷")
    @PostMapping("/delete/{examId}")
    public Result delete(@PathVariable Integer examId){
        Exam exam = examService.getById(examId);
        Integer userId = UserAuthUtil.getUserId();
        if(exam==null||exam.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        examService.removeById(examId);
        return Result.msgSuccess("删除成功");
    }
    @Operation(summary = "获取试卷信息")
    @GetMapping("/detail/{examId}")
    public Result info(@PathVariable Integer examId){
        Exam exam = examService.getById(examId);
        Integer userId = UserAuthUtil.getUserId();
        if(exam==null||exam.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        LambdaQueryWrapper<ExamQuestion> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ExamQuestion::getExamId,examId);
        List<ExamQuestion> list = examQuestionService.list(queryWrapper);
        ExamPaperVo paper=new ExamPaperVo();
        paper.setExam(exam);
        paper.setQuestions(list.stream().map(item->item.getQuestionId()).collect(Collectors.toSet()));
        return Result.success(paper);
    }
    @Operation(summary = "获取试卷题目信息")
    @GetMapping("/quesiton/{examId}")
    public Result quesiton(@PathVariable Integer examId){
        Exam exam = examService.getById(examId);
        Integer userId = UserAuthUtil.getUserId();
        if(exam==null||exam.getTeacherId()!=userId){
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
        LambdaQueryWrapper<Exam> queryWrapper=new LambdaQueryWrapper();
        Map<SFunction<Exam, ?>, Object> queryMap=new HashMap<>();
        queryMap.put(Exam::getCourseId,courseId);
        queryMap.put(Exam::getTeacherId,userId);
        queryWrapper.allEq(queryMap);
        Page<Exam> pa=new Page(page,pageSize);
        Page<Exam> record = examService.page(pa, queryWrapper);
        return Result.success(PageResult.setResult(record));
    }
}
