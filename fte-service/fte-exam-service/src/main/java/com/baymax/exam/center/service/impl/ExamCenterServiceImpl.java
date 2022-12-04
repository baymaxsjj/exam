package com.baymax.exam.center.service.impl;

import com.baymax.exam.center.model.ExamInfo;
import com.baymax.exam.center.model.Question;
import com.baymax.exam.center.model.QuestionItem;
import com.baymax.exam.center.utils.ExamRedisKey;
import com.baymax.exam.center.vo.QuestionInfoVo;
import com.baymax.exam.common.redis.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    RedisUtil redisUtil;

    /**
     * 缓存问题
     *
     * @return {@link List}<{@link Question}>
     */
    public List<QuestionInfoVo> getCacheQuestionsInfo(int examInfoId,int examPaperId){
        String key = ExamRedisKey.examQuestionsKey(examInfoId);
        if(redisUtil.hasKey(key)){
            return redisUtil.getCacheList(key);
        }
        List<QuestionInfoVo> questionInfo = questionService.examQuestionInfo(examPaperId);
        redisUtil.setCacheList(key,questionInfo);
        return questionInfo;
    }

    /**
     * 得到缓存考试信息
     *
     * @return {@link ExamInfo}
     */
    public ExamInfo getCacheExamInfo(int examInfoId){
        String key = ExamRedisKey.examInfoKey(examInfoId);
        if(redisUtil.hasKey(key)){
            return redisUtil.getCacheObject(key);
        }
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        final LocalDateTime now = LocalDateTime.now();
        //只在考试期间缓存
        if(now.isAfter(examInfo.getStartTime())&&now.isBefore(examInfo.getEndTime())){
            redisUtil.setCacheObject(key,examInfo);
        }
        return examInfo;
    }

}
