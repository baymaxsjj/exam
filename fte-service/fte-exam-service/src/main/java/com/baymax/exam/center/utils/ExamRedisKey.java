package com.baymax.exam.center.utils;


import static com.baymax.exam.common.core.base.RedisKeyConstants.RDS_EXAM_ONLINE_KEY;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/4 19:40
 * @description：缓存key规则
 * @modified By：
 * @version:
 *考试相关缓存规则
 * 固定key:
 *      examInfoId:
 *          questionInfoList：题目列表
 *          examInfo：考试信息
 *          students:
 *              userId:
 *                  questionSort:题目序号
 *                  optionSort:选项序号
 *                  sort:作答答案
 *
 *
 */
public class ExamRedisKey {
    //一级文件

    /**
     * 考试题目缓存key
     *
     * @param examInfoId 考试信息id
     * @return {@link String}
     */
    public static String examQuestionsKey(int examInfoId){
        return examOnLineKey(examInfoId)+":questions";
    }

    /**
     * 考试信息键
     *
     * @param examInfoId 考试信息id
     * @return {@link String}
     */
    public static String examInfoKey(int examInfoId){
        return examOnLineKey(examInfoId)+":examInfo";
    }
    //二级文件

    /**
     * 用户作答基本缓存key
     *
     * @param examInfoId 考试信息id
     * @return {@link String}
     */
    private static String examBaseStudentKey(int examInfoId,int userId){
        return examOnLineKey(examInfoId)+":students"+":"+userId;
    }

    /**
     * 学生考试题目顺序键
     *
     * @param examInfoId 考试信息id
     * @param userId     用户id
     * @return {@link String}
     */
    public static String examStudentQuestionsInfoKey(int examInfoId,int userId){
        return examBaseStudentKey(examInfoId,userId)+":questions";
    }

    /**
     * 学生考试答案键
     *
     * @param examInfoId 考试信息id
     * @param userId     用户id
     * @return {@link String}
     */
    public static String examStudentAnswerKey(int examInfoId,int userId){
        return examBaseStudentKey(examInfoId,userId)+":answers";
    }

    private static String examOnLineKey(int examInfoId){
        return RDS_EXAM_ONLINE_KEY+examInfoId;
    }
}
