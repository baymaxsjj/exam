package com.baymax.exam.center.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baymax.exam.center.enums.QuestionTypeEnum;
import com.baymax.exam.center.model.*;
import com.baymax.exam.center.service.impl.ExamClassServiceImpl;
import com.baymax.exam.center.service.impl.ExamInfoServiceImpl;
import com.baymax.exam.center.service.impl.ExamQuestionServiceImpl;
import com.baymax.exam.center.service.impl.ExamServiceImpl;
import com.baymax.exam.center.vo.ExamInfoVo;
import com.baymax.exam.center.vo.ExamPaperVo;
import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.user.feign.UserServiceClient;
import com.baymax.exam.user.model.Courses;
import com.baymax.exam.user.model.JoinClass;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * <p>
 * 考试信息 前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-10-28
 */
@Validated
@Tag(name = "考试管理")
@RestController
@RequestMapping("/exam-info")
public class ExamInfoController {
    @Autowired
    ExamInfoServiceImpl examInfoService;
    @Autowired
    ExamClassServiceImpl examClassService;
    @Autowired
    ExamServiceImpl examService;
    @Autowired
    ExamQuestionServiceImpl examQuestionService;

    @Autowired
    UserServiceClient userServiceClient;

    @Operation(summary = "发布考试信息")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated ExamInfoVo examInfoVo){
        ExamInfo examInfo=examInfoVo.getExamInfo();
        String info = "添加成功";
        Integer teacherId = null;
        //更改，以前那种可能会查询两次数据库
        if (examInfo.getId() != null) {
            ExamInfo temExam = examInfoService.getById(examInfo.getId());
            if (temExam != null) {
                teacherId = temExam.getTeacherId();
            }
            info = "更新成功";
        } else {
            Courses course = userServiceClient.findCourse(examInfo.getCourseId());
            if (course != null) {
                teacherId = course.getUserId();
            }
        }
        Integer userId = UserAuthUtil.getUserId();
        if (teacherId != userId) {
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //试卷是不是我的
        Exam exam = examService.getById(examInfo.getExamId());
        if(exam.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //TODO:班级是不是我的＞﹏＜
        //TODO:如果考试开始了，就不能更改试卷了

        //删除试卷考试班级，然后在添加
        if (examInfo.getId()  != null) {
            LambdaQueryWrapper<ExamClass> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(ExamClass::getExamInfoId,examInfo.getId());
            examClassService.remove(queryWrapper);
        }
        examInfo.setTeacherId(userId);
        //更新试卷信息
        examInfoService.saveOrUpdate(examInfo);
        //重新添加考试班级
        List<ExamClass> list=examInfoVo.getClassList().stream().map(integer ->{
            ExamClass examClass = new ExamClass();
            examClass.setClassId(integer);
            examClass.setExamInfoId(examInfo.getId());
            return examClass;
        }).collect(Collectors.toList());
        examClassService.saveBatch(list);
        return Result.msgSuccess(info);
    }
    @Operation(summary = "获取考试信息")
    @GetMapping("/detail/{examInfoId}")
    public Result<ExamInfoVo> detail(@PathVariable Integer examInfoId){
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        Integer userId = UserAuthUtil.getUserId();
        if(examInfo==null||examInfo.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        LambdaQueryWrapper<ExamClass> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ExamClass::getExamInfoId,examInfoId);
        List<ExamClass> list = examClassService.list(queryWrapper);
        ExamInfoVo paper=new ExamInfoVo();
        paper.setExamInfo(examInfo);

        paper.setClassList(list.stream().map(item->item.getClassId()).collect(Collectors.toSet()));
        return Result.success(paper);
    }

    @Operation(summary = "删除考试")
    @PostMapping("/delete/{examInfoId}")
    public Result delete(@PathVariable Integer examInfoId){
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        Integer userId = UserAuthUtil.getUserId();
        if(examInfo==null||examInfo.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        examInfoService.removeById(examInfoId);
        return Result.msgSuccess("删除成功");
    }

    @Operation(summary = "获取考试列表")
    @GetMapping("/list/{courseId}")
    public Result list(@PathVariable Integer courseId,
                       @Schema(description = "0:全部、1：未开始、2：进行中、3：结束")
                       @RequestParam(defaultValue = "0",required = false) Integer status,
                       @RequestParam(defaultValue = "1",required = false) Integer page,
                       @RequestParam(defaultValue = "10",required = false) Integer pageSize
                       ){
        Integer userId = UserAuthUtil.getUserId();
        Courses course = userServiceClient.findCourse(courseId);
        if(course==null){
            return Result.failed(ResultCode.PARAM_ERROR);
        }

        QueryWrapper<ExamInfo> queryWrapper=new QueryWrapper();
        switch (status){
            case 1:
                //未开始
                queryWrapper.apply(true,
                                "date_format (start_time,'%Y-%m-%d %H:%i:%S') > date_format(now(),'%Y-%m-%d %H:%i:%S')");

                break;
            case 2:
                //进行中
                queryWrapper.apply(true,
                        "date_format (start_time,'%Y-%m-%d %H:%i:%S')<= date_format(now(),'%Y-%m-%d %H:%i:%S')")
                .apply(true,
                        "date_format (end_time,'%Y-%m-%d %H:%i:%S') >= date_format(now(),'%Y-%m-%d %H:%i:%S')");
                break;
            case 3:
                //结束
                queryWrapper.apply(true,
                        "date_format (end_time,'%Y-%m-%d %H:%i:%S') < date_format(now(),'%Y-%m-%d %H:%i:%S')");
                break;
        }
        queryWrapper.orderByDesc("created_at");
        Page<ExamInfo> pa =new Page(page,pageSize);
        IPage<ExamInfo> record;
        if(course.getUserId()==userId){
            Map<String, Object> queryMap=new HashMap<>();
            queryMap.put("course_id",courseId);
            queryMap.put("teacher_id",userId);
            queryWrapper.allEq(queryMap);
            record= examInfoService.page(pa, queryWrapper);
        }else{
            JoinClass joinClass = userServiceClient.joinCourseByStuId(courseId, userId);
            if(joinClass==null){
                return Result.failed(ResultCode.PARAM_ERROR);
            }
            queryWrapper.eq("class_id",joinClass.getClassId());
            record=examInfoService.getSutExamInfo(pa,queryWrapper);
        }
        return Result.success(PageResult.setResult(record));
    }
    @Operation(summary = "开始考试")
    @GetMapping("/start/{examInfoId}")
    public Result startExam(@PathVariable Integer examInfoId){
        //获取考试信息
        ExamInfo examInfo = examInfoService.getById(examInfoId);

        Integer courseId = examInfo.getCourseId();
        //试卷id
        Integer userId = UserAuthUtil.getUserId();
        JoinClass joinClass = userServiceClient.joinCourseByStuId(courseId, userId);
        if(joinClass==null){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        Integer examId = examInfo.getExamId();
        //1. 查询 试卷题目
        List<Question> questionList = examQuestionService.getQuestionByExamId(examId);
        if(examInfo.getQuestionDisorder()){
            //打乱集合
            Collections.shuffle(questionList);
        }
        //2.题目类型分类
        Map<QuestionTypeEnum,List<Question>> questionTypeList=questionList.stream().collect(Collectors.groupingBy(Question::getType));
        // 存入缓存
        Map<String,Object> result=new HashMap<>();
        result.put("examInfo",examInfo);
        result.put("questionList",questionTypeList);
        return  Result.success(result);
    }
}