package com.baymax.exam.center.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baymax.exam.center.enums.DefaultQuestionRuleEnum;
import com.baymax.exam.center.model.Question;
import com.baymax.exam.center.service.impl.QuestionServiceImpl;
import com.baymax.exam.center.utils.ParseQuestionText;
import com.baymax.exam.center.model.ParseQuestionRules;
import com.baymax.exam.center.vo.BatchQuestion;
import com.baymax.exam.center.vo.ParseQuestionVo;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 题目信息 前端控制器
 * </p>
 *规定：所有类型题目至少要有一下选项，如主观题，就算没有答案，都要给一个选项
 * @author baymax
 * @since 2022-10-18
 */
@Slf4j
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
        String result = questionService.addQuestion(questionInfo);
        if("".equals(result)){
            return Result.msgSuccess("创建题目成功");
        }else{
            return Result.msgError(result);
        }
    }
    @Operation(summary = "批量创建题目")
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody @Validated BatchQuestion batchQuestion){
        Courses course = userServiceClient.findCourse(batchQuestion.getCourseId());
        Integer userId = UserAuthUtil.getUserId();
        if(course==null||course.getUserId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        List<QuestionInfoVo> list=batchQuestion.getQuestionInfos();
        List<String> result=new ArrayList<>();
        list.stream().forEach(i->{
            i.setTeacherId(userId);
            i.setTagId(batchQuestion.getTagId());
            i.setCourseId(batchQuestion.getCourseId());
            result.add( questionService.addQuestion(i));
        });
        return Result.success(result);
    }
    @Operation(summary = "获取匹配题目规则")
    @GetMapping("/rules")
    public Result rules(){
        Map<String, String> collect = Arrays.stream(DefaultQuestionRuleEnum.values()).collect(Collectors.toMap(DefaultQuestionRuleEnum::name, DefaultQuestionRuleEnum::getName));
        return Result.success(collect);
    }
    @Operation(summary = "解析题目文本")
    @PostMapping("/analyze")
    public Result analyze(@RequestBody @Validated ParseQuestionVo parseQuestionVo){
        // TODO:还是后端呢？
        ParseQuestionRules rule= DefaultQuestionRuleEnum.CHAOXING.getRule();
        if(parseQuestionVo.getCustomRule()!=null){
            rule= parseQuestionVo.getCustomRule();
        }else if(parseQuestionVo.getDefaultRule()!=null){
            rule = parseQuestionVo.getDefaultRule().getRule();
        }
        //将富文本换行改成\n
        String text= parseQuestionVo.getQuestionsText().replaceAll("<br\\/?>","\n");
        //去除富文本最外层p
        text=text.replaceAll("^<p>|<\\/p>$","");
        return Result.success( ParseQuestionText.parse(text,rule));
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
