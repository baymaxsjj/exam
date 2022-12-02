package com.baymax.exam.center.utils;


import static com.baymax.exam.common.core.base.RedisKeyConstants.RDS_EXAM_ONLINE_KEY;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/4 19:40
 * @description：缓存key规则
 * @modified By：
 * @version:
 */
public class RedisKeyRule {
    /**
     * 考试题目缓存key
     *
     * @param examInfoId 考试信息id
     * @param userId     用户id
     * @return {@link String}
     */
    public static String examQuestionKey(int examInfoId,int userId){
        return examOnLineKey(examInfoId)+":"+userId+":question";
    }

    /**
     * 考试答案缓存key
     *
     * @param examInfoId 考试信息id
     * @param userId     用户id
     * @return {@link String}
     */
    public static String examAnswerKey(int examInfoId,int userId){
        return examOnLineKey(examInfoId)+":"+userId+":answer";
    }

    /**
     * 考试选项缓存key
     *
     * @param examInfoId 考试信息id
     * @param userId     用户id
     * @return {@link String}
     */
    public static String examOptionsKey(int examInfoId,int userId){
        return examOnLineKey(examInfoId)+":"+userId+":options";
    }
    private static String examOnLineKey(int examInfoId){
        return RDS_EXAM_ONLINE_KEY+examInfoId;
    }
}
