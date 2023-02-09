package com.baymax.exam.center.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baymax.exam.center.enums.QuestionClassificationTypeEnum;
import com.baymax.exam.center.enums.QuestionResultTypeEnum;
import com.baymax.exam.center.enums.QuestionTypeEnum;
import com.baymax.exam.center.enums.ReviewTypeEnum;
import com.baymax.exam.center.model.*;
import com.baymax.exam.center.utils.ExamRedisKey;
import com.baymax.exam.center.vo.ExamAnswerInfoVo;
import com.baymax.exam.center.vo.QuestionInfoVo;
import com.baymax.exam.center.vo.StudentReviewVo;
import com.baymax.exam.common.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author ：Baymax
 * @date ：Created in 2022/12/4 13:23
 * @description：考试中心服务
 * @modified By：
 * @version:
 */
@Slf4j
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
    public boolean deleteCacheExamInfo(int examInfoId){
        String key = ExamRedisKey.examInfoKey(examInfoId);
        return redisUtils.deleteObject(key);
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
        if(examInfo==null){
            return null;
        }
        final LocalDateTime now = LocalDateTime.now();
        //只在考试期间缓存
        if(now.isAfter(examInfo.getStartTime())&&now.isBefore(examInfo.getEndTime())){
            redisUtils.setCacheObject(key,examInfo);
        }
        return examInfo;
    }
    public TreeMap<QuestionTypeEnum, List<QuestionInfoVo>> getGroupQuestionList(List<QuestionInfoVo> questionInfoList){
        final Map<QuestionTypeEnum, List<QuestionInfoVo>> questionGroup = questionInfoList.stream().collect(Collectors.groupingBy(Question::getType));
        //题目类型排序
        TreeMap<QuestionTypeEnum, List<QuestionInfoVo>> questionOrderGroup=new TreeMap<>(Comparator.comparing(QuestionTypeEnum::getValue));
        questionOrderGroup.putAll(questionGroup);
        return questionOrderGroup;
    }
    public void robotReview(){

    }

    /**
     * 回答比较
     * 系统对答案选项值给出正确、错误、半错、未选等请求说明
     * 得分应是算在题目上，而不是选项上,
     * 个人答案屏蔽null
     *
     * @param questionInfos 问题信息
     * @param answerResults 回答结果
     * @return {@link List}<{@link ExamAnswerInfoVo}>
     */
    public List<ExamAnswerInfoVo> answerCompare(List<QuestionInfoVo> questionInfos, List<ExamAnswerResult> answerResults){
        List<ExamAnswerInfoVo> list=new ArrayList<>();
        //按题目id分组
        final Map<Integer, List<ExamAnswerResult>> optionsMap = answerResults.stream().collect(Collectors.groupingBy(ExamAnswerResult::getQuestionId));
       questionInfos.forEach(question->{
           //获取该题目的所有作答序列
           if(optionsMap.containsKey(question.getId())){
               log.info("\n");
               log.info("------------------机器评阅------------------");
               log.info("题目信息：id：{}、分值：{}",question.getId(),question.getScore());
               ExamAnswerInfoVo answerInfo=new ExamAnswerInfoVo();
               //得分记录
               ExamScoreRecord scoreRecord=new ExamScoreRecord();
               //学生作答信息,过滤不为nul答答案
               List<ExamAnswerResult> results=optionsMap.get(question.getId()).stream().filter(r->r.getAnswer()!=null).collect(Collectors.toList());
               //题目选项信息
               List<QuestionItem> options = question.getOptions();
               QuestionTypeEnum type=question.getType();
               AtomicReference<Float> score= new AtomicReference<>(0f);
                if(type==QuestionTypeEnum.SIGNAL_CHOICE||type==QuestionTypeEnum.JUDGMENTAL){
                    //只能一个选项
                    if(results.size()==1){
                        results.forEach(result -> {
                            options.stream().filter(o-> Objects.equals(o.getId(), result.getOptionId())).findFirst().ifPresent(option->{
                                log.info("单选/判断：选项答案:{}、作答答案:{}",option.getAnswer(),result.getAnswer());
                                if(Objects.equals(option.getAnswer(),result.getAnswer())){
                                    log.info("单选/判断：批评结果：正确");
                                    result.setResultType(QuestionResultTypeEnum.CORRECT);
                                    score.set(question.getScore());
                                }else{
                                    result.setResultType(QuestionResultTypeEnum.ERROR);
                                    log.info("单选/判断：批评结果：错误");
                                }
                            });
                        });
                     }
                }else if(type==QuestionTypeEnum.MULTIPLE_CHOICE){
                    //多选题，错了全错，全队满分，半对看着给
                    AtomicBoolean isError= new AtomicBoolean(false);
                    AtomicInteger rCount= new AtomicInteger();
                    int oCount = (int)options.stream().filter(o -> o.getAnswer() != null).count();
                    float averageScore=question.getScore()/oCount;
                    //获取作答答案
                    results.forEach(result -> {
                        options.stream().filter(o-> Objects.equals(o.getId(), result.getOptionId())).findFirst().ifPresent(option->{
                            //获取到正确选项
                            if (result.getAnswer() != null) {
                                //判断正确
                                log.info("多选：选项答案:{}、作答答案:{}", option.getAnswer(), result.getAnswer());
                                if (Objects.equals(result.getAnswer(), option.getAnswer())) {
                                    log.info("多选：批评结果：正确");
                                    result.setResultType(QuestionResultTypeEnum.CORRECT);
                                    rCount.getAndIncrement();
                                    score.set((score.get() + averageScore));
                                } else {
                                    log.info("多选：批评结果：多选错误");
                                    //作答错误，结束
                                    result.setResultType(QuestionResultTypeEnum.ERROR);
                                    score.set(0f);
                                    isError.set(true);
                                }
                            }
                        });
                    });
                    //选错全错
                    if(!isError.get() &&(rCount.get() ==oCount)){
                        //全选对满分
                        log.info("多选：批评结果：全对。满分");
                        score.set(question.getScore());
                    }
                //填空
                }else if(type==QuestionTypeEnum.COMPLETION||type==QuestionTypeEnum.SUBJECTIVE){
                    int oCount = options.size();
                    AtomicInteger rCount= new AtomicInteger();
                    float averageScore=question.getScore()/oCount;
                    results.forEach(result->{
                        options.stream().filter(o-> Objects.equals(o.getId(), result.getOptionId())).findFirst().ifPresent(option->{
                            String htmlTag="<[^>]+>";
                            String rAnswer=result.getAnswer().replaceAll(htmlTag,"").trim();
                            String oAnswer=option.getAnswer().replaceAll(htmlTag,"").trim();
                            log.info("主观/填空：选项答案:{}、作答答案:{}",oAnswer,rAnswer);
                            if(Objects.equals(rAnswer,oAnswer)){
                                log.info("主观/填空：批评结果：正确");
                                score.set(score.get()+averageScore);
                                result.setResultType(QuestionResultTypeEnum.CORRECT);
                                rCount.getAndIncrement();
                            }else{
                                result.setResultType(QuestionResultTypeEnum.ERROR);
                            }
                        });
                    });
                    if(oCount==rCount.get()){
                        score.set(question.getScore());
                    }
                }
                if(score.get()==0f){
                    scoreRecord.setResultType(QuestionResultTypeEnum.ERROR);
                }else if(score.get()==question.getScore()){
                    scoreRecord.setResultType(QuestionResultTypeEnum.CORRECT);
                }else{
                    scoreRecord.setResultType(QuestionResultTypeEnum.WRONG);
                }
               log.info("得分：{}",score.get());
                if(!results.isEmpty()){
                    scoreRecord.setScore(score.get());
                    scoreRecord.setQuestionId(question.getId());
                    scoreRecord.setUserId(results.get(0).getUserId());
                    scoreRecord.setExamInfoId(results.get(0).getExamInfoId());
                    scoreRecord.setReviewType(ReviewTypeEnum.ROBOT);
                    answerInfo.setScoreRecord(scoreRecord);
                    answerInfo.setAnswerResult(results);
                    list.add(answerInfo);
                }
           }
       });
       log.info("机器评判结果：{}", JSON.toJSON(list));
       return list;
    }
    public void statisticalAnswerInfo(StudentReviewVo studentReview, List<ExamScoreRecord> scoreList, List<QuestionInfoVo> questionInfoVos){
        AtomicReference<Float> sumScore= new AtomicReference<>((float) 0);
        AtomicInteger correctNumber= new AtomicInteger();
        AtomicInteger reviewCount= new AtomicInteger();
        AtomicInteger reviewTotal= new AtomicInteger();
        scoreList.forEach(examScoreRecord -> {
            //计算得分
            sumScore.updateAndGet(v ->(v + examScoreRecord.getScore()));
            //计算正确数目
            if(examScoreRecord.getResultType()==QuestionResultTypeEnum.CORRECT){
                correctNumber.getAndIncrement();
            }
            //计算批阅总数
            questionInfoVos.stream().filter(questionInfoVo -> Objects.equals(questionInfoVo.getId(), examScoreRecord.getQuestionId())).findFirst().ifPresent(questionInfoVo -> {
                QuestionClassificationTypeEnum classificationType = questionInfoVo.getType().getClassificationType();
                if(classificationType==QuestionClassificationTypeEnum.SUBJECTIVE){
                    reviewTotal.getAndIncrement();
                    if(examScoreRecord.getReviewType()==ReviewTypeEnum.TEACHER){
                        reviewCount.getAndIncrement();
                    }
                }
            });

        });
        studentReview.setScore(sumScore.get());
        studentReview.setCorrectNumber(correctNumber.get());
        studentReview.setReviewCount(reviewCount.get());
        studentReview.setReviewTotal(reviewTotal.get());
    }



}
