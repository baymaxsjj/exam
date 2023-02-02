package com.baymax.exam.center.controller;

import com.baymax.exam.center.enums.ExamAnswerLogEnum;
import com.baymax.exam.center.enums.QuestionResultTypeEnum;
import com.baymax.exam.center.enums.QuestionTypeEnum;
import com.baymax.exam.center.model.*;
import com.baymax.exam.center.service.impl.*;
import com.baymax.exam.center.utils.ExamRedisKey;
import com.baymax.exam.center.vo.ExamAnswerInfoVo;
import com.baymax.exam.center.vo.QuestionInfoVo;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.redis.utils.RedisUtils;
import com.baymax.exam.user.feign.UserClient;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.baymax.exam.center.interceptor.ExamCenterInterceptor.EXAM_INFO_KEY;

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
    ExamCenterServiceImpl examCenterService;
    @Autowired
    ExamAnswerResultServiceImpl answerResultService;
    @Autowired
    ExamScoreRecordServiceImpl scoreRecordService;

    @Autowired
    UserClient userClient;
    @Autowired
    HttpServletRequest request;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    ExamAnswerLogServiceImpl examAnswerLogService;



    @Operation(summary = "开始考试")
    @GetMapping("/start")
    public Result startExam(@PathVariable Integer examInfoId){
        //FIXME:考试题目/选项，放入缓存。这里只存题目序号
        Integer userId = UserAuthUtil.getUserId();
        //获取考试信息
        ExamInfo examInfo = (ExamInfo) request.getAttribute(EXAM_INFO_KEY);
        //获取考试题目
        List<QuestionInfoVo> cacheQuestionsInfo = examCenterService.getCacheQuestionsInfo(examInfoId, examInfo.getExamId());
        //获取个人题目序号
        String key = ExamRedisKey.examStudentQuestionsInfoKey(examInfoId, userId);
        if(redisUtils.hasKey(key)){
            Map<String, Integer> questionOrders= redisUtils.getCacheMap(key);
            //还原题目顺序
            cacheQuestionsInfo.sort((o1, o2) -> {
                Integer w1=questionOrders.get(o1.getId().toString());
                Integer w2=questionOrders.get(o2.getId().toString());
                return w1.compareTo(w2);
            });
        }else{
            if(examInfo.getQuestionDisorder()){
                //打乱集合
                Collections.shuffle(cacheQuestionsInfo);
                //保存题目顺序
                final Map<String, Integer> orderWight = new HashMap<>();
                for (int i = 0; i < cacheQuestionsInfo.size(); i++) {
                    orderWight.put(cacheQuestionsInfo.get(i).getId().toString(),i);
                }
                redisUtils.setCacheMap(key,orderWight);
            }
        }
        //打乱题目选项
        if(examInfo.getOptionDisorder()){
            cacheQuestionsInfo.stream().forEach(q->{
                final QuestionTypeEnum type = q.getType();
                if(type==QuestionTypeEnum.MULTIPLE_CHOICE||type==QuestionTypeEnum.SIGNAL_CHOICE){
                    Collections.shuffle(q.getOptions());
                }
            });
        }
        String redisAnswerKey= ExamRedisKey.examStudentAnswerKey(examInfoId,userId);
        if(redisUtils.hasKey(redisAnswerKey)){
            //第n次访问，整合自己的答案
            Map<String,Map<Integer,String>> answerList = redisUtils.getCacheMap(redisAnswerKey);
            cacheQuestionsInfo.stream().forEach(q->{
                //得到个人答案
                Map<Integer, String> questionAnswer = answerList.get(q.getId().toString());
                q.getOptions().forEach(questionItem -> {
                    if(questionAnswer!=null&&questionAnswer.containsKey(questionItem.getId())){
                        String answer = questionAnswer.get(questionItem.getId());
                        questionItem.setAnswer(answer);
                    }else{
                        //去除答案
                        questionItem.setAnswer(null);
                    }
                });
            });
        }else{
            //第一次访问，清除所有答案
            cacheQuestionsInfo.stream().forEach(q->{
                q.getOptions().forEach(questionItem -> {
                    questionItem.setAnswer(null);
                });
            });
        }

        //2.题目类型分类
        //题目类型排序
        TreeMap<QuestionTypeEnum, List<QuestionInfoVo>> questionOrderGroup= examCenterService.getGroupQuestionList(cacheQuestionsInfo);
        examAnswerLogService.writeLog(userId,examInfo,ExamAnswerLogEnum.START,UserAuthUtil.getUserIp());
        Map<String,Object> result=new HashMap<>();
        result.put("examInfo",examInfo);
        result.put("systemTime",LocalDateTime.now());
        result.put("questionList",questionOrderGroup);
        return  Result.success(result);
    }
    @Operation(summary = "学生答案一致性确认")
    @GetMapping("/question/{questionId}")
    public Result option(@PathVariable Integer examInfoId, @PathVariable Integer questionId){
        //判断改题目是否在考试中
        Integer userId = UserAuthUtil.getUserId();
        //1.缓存获取答案
        String redisAnswerKey= ExamRedisKey.examStudentAnswerKey(examInfoId,userId);
        Map<String,Map<Integer,String>> answerList = redisUtils.getCacheMap(redisAnswerKey);
        //3.获取该题的作答
        Map<Integer, String> answer = answerList.get(questionId.toString());
        return  Result.success(answer);
    }
    @Operation(summary = "提交答案")
    @PostMapping("/answer/{questionId}")
    public Result answer(@PathVariable Integer examInfoId, @RequestBody Map<Integer,String> answerResult, @PathVariable String questionId){
        Integer userId = UserAuthUtil.getUserId();
        String redisAnswerKey= ExamRedisKey.examStudentAnswerKey(examInfoId,userId);
        //key
        Map<String,Map<Integer,String>> result;
        // 判断是不是考试题目
        if (redisUtils.hasKey(redisAnswerKey)){
            //1.获取缓存中的考试答案
            result= redisUtils.getCacheMap(redisAnswerKey);
        }else{
            result=new HashMap<>();
        }
        result.put(questionId,answerResult);
        log.info("提交答案："+result);
        //放入缓存，提交答案的时候在存到数据库
        redisUtils.setCacheMap(redisAnswerKey,result);
        return Result.success(answerResult.size());
    }
    @Operation(summary = "考试行为")
    @PostMapping("/action")
    public Result action(@PathVariable Integer examInfoId,@RequestBody ExamAnswerLog answerLog){
        ExamInfo examInfo = (ExamInfo) request.getAttribute(EXAM_INFO_KEY);
        final Boolean isMonitor = examInfo.getIsMonitor();
        if(isMonitor){
            Integer userId = UserAuthUtil.getUserId();
            examAnswerLogService.writeLog(userId,examInfo,answerLog.getStatus(),answerLog.getInfo());
        }
        //放入缓存，提交答案的时候在存到数据库
        return Result.success("提交成功");
    }
    @Operation(summary = "交卷")
    @PostMapping("/submit")
    public Result submit(@PathVariable Integer examInfoId){
        ExamInfo examInfo = (ExamInfo) request.getAttribute(EXAM_INFO_KEY);
        //放入缓存，提交答案的时候在存到数据库
        int userId = UserAuthUtil.getUserId();
        submit(userId,examInfo);
        return Result.success("交卷成功",null);
    }
    @Async
    public void submit(int userId,ExamInfo examInfo){
        //提交记录
        //获取题目
        examAnswerLogService.writeLog(userId,examInfo,ExamAnswerLogEnum.SUBMIT,UserAuthUtil.getUserIp());
        List<QuestionInfoVo> questionsInfo = examCenterService.getCacheQuestionsInfo(examInfo.getId(), examInfo.getExamId());
        String redisAnswerKey= ExamRedisKey.examStudentAnswerKey(examInfo.getId(),userId);
        List<ExamAnswerResult> answerResults=new ArrayList<>();
        if (redisUtils.hasKey(redisAnswerKey)) {
            //1.获取缓存中的考试答案
            Map<String,Map<Integer,String>> results= redisUtils.getCacheMap(redisAnswerKey);
            //答案封装
            results.forEach((questionId,answers)->{
                answers.forEach((optionId,answer)->{
                    ExamAnswerResult result=new ExamAnswerResult();
                    result.setUserId(userId);
                    result.setExamInfoId(examInfo.getId());
                    result.setQuestionId(Integer.valueOf(questionId));
                    result.setOptionId(Integer.valueOf(optionId));
                    result.setAnswer(answer);
                    answerResults.add(result);
                });
            });
        }
        List<ExamAnswerInfoVo> examAnswerInfo = examCenterService.answerCompare(questionsInfo, answerResults);
        List<ExamAnswerResult> answerResult = examAnswerInfo.stream().flatMap(info -> info.getAnswerResult().stream()).collect(Collectors.toList());
        List<ExamScoreRecord> scoreRecords = examAnswerInfo.stream().map(info -> info.getScoreRecord()).collect(Collectors.toList());
        scoreRecordService.saveBatch(scoreRecords);
        answerResultService.saveBatch(answerResult);
//        examAnswerLogService.writeLog(userId,examInfo,ExamAnswerLogEnum.ROBOT_REVIEW,null);
        examAnswerLogService.saveReviewStatus(examInfo.getId(),userId,ExamAnswerLogEnum.ROBOT_REVIEW);
        //删除缓存
        String questionsKey = ExamRedisKey.examStudentQuestionsInfoKey(examInfo.getId(), userId);
        redisUtils.deleteObject(redisAnswerKey);
        redisUtils.deleteObject(questionsKey);
    }
}
