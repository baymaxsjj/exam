package com.baymax.exam.center.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baymax.exam.center.model.Question;
import com.baymax.exam.center.model.Tags;
import com.baymax.exam.center.service.impl.QuestionServiceImpl;
import com.baymax.exam.center.vo.QuestionInfoVo;
import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.user.feign.UserServiceClient;
import com.baymax.exam.user.model.Courses;
import com.baymax.exam.user.model.JoinClass;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 题目信息 前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-10-18
 */
@Validated
@Tag(name = "题目管理")
@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    QuestionServiceImpl questionService;
    @Autowired
    UserServiceClient userServiceClient;
    @Operation(summary = "创建题目")
    @PostMapping("/add")
    public Result add(@RequestBody @Validated QuestionInfoVo questionInfo){
        //判断课程是不是自己的
        Courses course = userServiceClient.findCourse(questionInfo.getCourseId());
        Integer userId = UserAuthUtil.getUserId();
        if(course==null||course.getUserId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        questionInfo.setTeacherId(userId);
        questionInfo.setId(null);
        boolean boo = questionService.addQuestion(questionInfo);
        if(!boo){
            return Result.msgSuccess("创建题目失败");
        }
        return Result.msgSuccess("创建题目成功");
    }
    @Operation(summary = "批量创建题目")
    @PostMapping("/batchAdd/{courseId}")
    public Result batchAdd(@RequestBody @Validated QuestionInfoVo questionInfo,@PathVariable String courseId){
        // TODO:前端处理呢！还是后端呢？
        return Result.msgSuccess("更新成功");
    }

    @Operation(summary = "更新题目")
    @PostMapping("/update")
    public Result update(@RequestBody Question question){
        //判断题目是不是自己的
        Question qu = questionService.getById(question.getId());
        Integer userId = UserAuthUtil.getUserId();
        if(qu==null||qu.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        question.setCourseId(null);
        question.setTeacherId(null);
        questionService.updateById(question);
        return Result.msgSuccess("更新成功");
    }
    @Operation(summary = "删除题目")
    @PostMapping("/delete/{questionId}")
    public Result delete(@PathVariable String questionId){
        //判断题目是不是自己的
        Question qu = questionService.getById(questionId);
        Integer userId = UserAuthUtil.getUserId();
        if(qu==null||qu.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        questionService.removeById(questionId);
        return Result.msgSuccess("删除成功");
    }
    @Operation(summary = "题目列表")
    @GetMapping("/list/{courseId}")
    public Result list(
            @PathVariable Integer courseId,
            @RequestParam Integer currentPage,
            @RequestParam(required = false) Integer tagId){
        Courses course = userServiceClient.findCourse(courseId);
        Integer userId = UserAuthUtil.getUserId();
        if(course==null){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        // 不需要查分组是不是自己的，因为，下面查询时课程id 和分组id 同时成立才行
        LambdaQueryWrapper<Question> queryWrapper=new LambdaQueryWrapper<>();
        //老师
        Map<SFunction<Question, ?>, Object> queryMap=new HashMap<>();
        queryMap.put(Question::getTagId,tagId);
        queryMap.put(Question::getCourseId,courseId);
        if(course.getUserId()!=userId){
            //1.判断是否课程班级中
            JoinClass joinClass = userServiceClient.joinCourseByStuId(courseId, userId);
            if(joinClass==null){
                return Result.failed(ResultCode.PARAM_ERROR);
            }
            //TODO:判断该班级是否在考试，如果考试禁止获取
            //2.查找有公开题目的分类
            queryWrapper.gt(Question::getIsPublic,0);
        }
        queryWrapper.allEq(queryMap).orderByDesc(Question::getCreatedAt);
        IPage<Question> page=new Page<>(currentPage,10);
        IPage<Question> list = questionService.page(page,queryWrapper);
        return Result.success(PageResult.setResult(list));
    }
    @Operation(summary = "题目详情")
    @GetMapping("/detail/{questionId}")
    public Result<QuestionInfoVo> detail(
            @PathVariable Integer questionId){
        Question question = questionService.getById(questionId);
        Integer userId = UserAuthUtil.getUserId();
        // 是否有查看权限
        if(question.getTeacherId()!=userId){
            //1.判断是否课程班级中
            JoinClass joinClass = userServiceClient.joinCourseByStuId(question.getCourseId(), userId);
            if(joinClass==null){
                return Result.failed(ResultCode.PARAM_ERROR);
            }
            //TODO:判断该班级是否在考试，如果考试禁止获取
            //2.查找有公开题目的分类
        }
        QuestionInfoVo questionInfo = questionService.questionInfo(questionId);
        return Result.success(questionInfo);
    }
}
