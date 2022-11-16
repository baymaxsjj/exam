package com.baymax.exam.center.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baymax.exam.center.enums.QuestionTypeEnum;
import com.baymax.exam.center.model.*;
import com.baymax.exam.center.service.impl.*;
import com.baymax.exam.center.utils.RedisKeyRule;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.common.redis.utils.RedisUtil;
import com.baymax.exam.user.feign.UserServiceClient;
import com.baymax.exam.user.model.JoinClass;
import com.baymax.exam.user.model.User;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.baymax.exam.center.interceptor.ExamCenterInterceptor.EXAM_INFO_KEY;
import static com.baymax.exam.common.core.base.ExamAuth.*;

/**
 * 缓存处理建议
 *
 * @author ：Baymax
 * @date ：Created in 2022/10/31 18:19
 * @description：考试中心
 * @modified By：
 * @version:
 */
@Slf4j
@Validated
@Tag(name = "考试中心")
@RestController
@RequestMapping("/exam-center/{examInfoId}")
public class ExamCenterController {

    @Autowired
    ExamQuestionServiceImpl examQuestionService;
    @Autowired
    QuestionItemServiceImpl questionItemService;

    @Autowired
    UserServiceClient userServiceClient;
    @Autowired
    HttpServletRequest request;
    @Autowired
    RedisUtil redisUtil;

    @Operation(summary = "开始考试")
    @GetMapping("/start")
    public Result startExam(@PathVariable String examInfoId){
        Integer userId = UserAuthUtil.getUserId();
        //获取考试信息
        ExamInfo examInfo = (ExamInfo) request.getAttribute(EXAM_INFO_KEY);
        Integer examId = examInfo.getExamId();
        String redisAnswerKey= RedisKeyRule.examQuestionKey(examInfo.getId(),userId);
        //0.从缓存获取
        Map<String,List<Question>> questionTypeList;
        if(redisUtil.hasKey(redisAnswerKey)){
            questionTypeList = redisUtil.getCacheMap(redisAnswerKey);
        }else{
            //1. 查询 试卷题目
            List<Question> questionList = examQuestionService.getQuestionByExamId(examId);
            if(examInfo.getQuestionDisorder()){
                //打乱集合
                Collections.shuffle(questionList);
            }
            questionList.stream().forEach(question -> question.setAnalysis(null));
            //2.题目类型分类
            questionTypeList=questionList.stream().collect(Collectors.groupingBy(i->i.getType().name()));
            //3.放入缓存
            redisUtil.setCacheMap(redisAnswerKey,questionTypeList);
        }
        Map<String,Object> result=new HashMap<>();
        result.put("examInfo",examInfo);
        result.put("systemTime",LocalDateTime.now());
        result.put("questionList",questionTypeList);
        return  Result.success(result);
    }
    @Operation(summary = "考试选项")
    @GetMapping("/question/{questionId}")
    public Result option(@PathVariable Integer examInfoId, @PathVariable Integer questionId){
        //判断改题目是否在考试中
        ExamInfo examInfo = (ExamInfo) request.getAttribute(EXAM_INFO_KEY);
        Integer examId = examInfo.getExamId();
        ExamQuestion examQuestion = examQuestionService.getExamQuestion(examId, questionId);
        if(examQuestion==null){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        Integer userId = UserAuthUtil.getUserId();

        //1.缓存获取答案
        String redisAnswerKey=RedisKeyRule.examAnswerKey(examInfoId,userId);
        Map<String,Map<Integer,String>> answerList = redisUtil.getCacheMap(redisAnswerKey);
        log.info("缓存答案："+answerList);
        //2.获取选项
        List<QuestionItem> questionItems = questionItemService.getQuestionItems(questionId);
        //3.获取该题的作答
        Map<Integer, String> answer = answerList.get(questionId.toString());
        log.info("该题目缓存答案："+questionId+"->"+answer);
        //TODO:3.判断选项要不要乱序,这样还要判断题目类型哭~~~算了先放着吧
        questionItems.stream().forEach(questionItem -> {
            if(answer!=null){
                questionItem.setAnswer(answer.get(questionItem.getId()));
            }else{
                questionItem.setAnswer(null);
            }
        });
        return  Result.success(questionItems);
    }
    @Operation(summary = "提交答案")
    @PostMapping("/answer/{questionId}")
    public Result answer(@PathVariable Integer examInfoId, @RequestBody Map<Integer,String> answerResult, @PathVariable String questionId){
        Integer userId = UserAuthUtil.getUserId();
        String redisAnswerKey=RedisKeyRule.examAnswerKey(examInfoId,userId);
        //key
        Map<String,Map<Integer,String>> result;
        // 判断是不是考试题目
        if (redisUtil.hasKey(redisAnswerKey)){
            //1.获取缓存中的考试答案
            result=redisUtil.getCacheMap(redisAnswerKey);
        }else{
            result=new HashMap<>();
        }
        result.put(questionId,answerResult);
        log.info("提交答案："+result);
        //放入缓存，提交答案的时候在存到数据库
        redisUtil.setCacheMap(redisAnswerKey,result);
        return Result.success(answerResult.size());
    }
    @Operation(summary = "交卷")
    @PostMapping("/submit")
    public Result submit(@PathVariable Integer examInfoId){
        //放入缓存，提交答案的时候在存到数据库

        return Result.success("交卷成功",null);
    }
}
