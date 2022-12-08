package com.baymax.exam.center.service.impl;

import com.baymax.exam.center.enums.QuestionResultTypeEnum;
import com.baymax.exam.center.enums.QuestionTypeEnum;
import com.baymax.exam.center.model.ExamAnswerResult;
import com.baymax.exam.center.model.ExamInfo;
import com.baymax.exam.center.model.Question;
import com.baymax.exam.center.model.QuestionItem;
import com.baymax.exam.center.utils.ExamRedisKey;
import com.baymax.exam.center.vo.QuestionInfoVo;
import com.baymax.exam.common.redis.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：Baymax
 * @date ：Created in 2022/12/4 13:23
 * @description：考试中心服务
 * @modified By：
 * @version:
 */
@Service
public class ExamCenterServiceImpl {
    @Autowired
    QuestionItemServiceImpl questionItemService;
    @Autowired
    QuestionServiceImpl questionService;
    @Autowired
    ExamInfoServiceImpl examInfoService;
    @Autowired
    RedisUtils redisUtils;

    /**
     * 缓存问题
     *
     * @return {@link List}<{@link Question}>
     */
    public List<QuestionInfoVo> getCacheQuestionsInfo(int examInfoId,int examPaperId){
        String key = ExamRedisKey.examQuestionsKey(examInfoId);
        if(redisUtils.hasKey(key)){
            return redisUtils.getCacheList(key);
        }
        List<QuestionInfoVo> questionInfo = questionService.examQuestionInfo(examPaperId);
        redisUtils.setCacheList(key,questionInfo);
        return questionInfo;
    }

    /**
     * 得到缓存考试信息
     *
     * @return {@link ExamInfo}
     */
    public ExamInfo getCacheExamInfo(int examInfoId){
        String key = ExamRedisKey.examInfoKey(examInfoId);
        if(redisUtils.hasKey(key)){
            return redisUtils.getCacheObject(key);
        }
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        final LocalDateTime now = LocalDateTime.now();
        //只在考试期间缓存
        if(now.isAfter(examInfo.getStartTime())&&now.isBefore(examInfo.getEndTime())){
            redisUtils.setCacheObject(key,examInfo);
        }
        return examInfo;
    }
    public void robotReview(){

    }

    public List<ExamAnswerResult> answerCompare(List<QuestionInfoVo> questionInfos, List<ExamAnswerResult> answerResults){
        //按题目id分组
        final Map<Integer, List<ExamAnswerResult>> optionsMap = answerResults.stream().collect(Collectors.groupingBy(ExamAnswerResult::getQuestionId));
       questionInfos.forEach(question->{
           //获取该题目的所有作答序列
           if(optionsMap.containsKey(question.getId())){
               List<ExamAnswerResult> results=optionsMap.get(question.getId());
               List<QuestionItem> options = question.getOptions();
               int score=question.getScore();
               QuestionTypeEnum type=question.getType();
               //答案对比
               if(type!=QuestionTypeEnum.MULTIPLE_CHOICE){
                    float optionScore;
                    if(type==QuestionTypeEnum.COMPLETION||type==QuestionTypeEnum.SUBJECTIVE){
                        optionScore=score/options.size();
                    } else {
                        optionScore = score;
                    }
                   options.stream().forEach(option->{
                        String answer = option.getAnswer();
                        results.stream().filter(r->r.getOptionId()==option.getId()).findFirst().ifPresent(r->{
                             boolean equals = r.getAnswer().equals(answer);
                             if(equals){
                                 r.setScore(optionScore);
                                 r.setResultType(QuestionResultTypeEnum.CORRECT);
                             }else{
                                 r.setScore(0f);
                                 r.setResultType(QuestionResultTypeEnum.ERROR);
                             }
                        });
                    });
               }else {
                   //多选题判断
               }
           }
       });
       return answerResults;
    }
    public void teacherReview(){

    }



}
