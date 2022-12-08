package com.baymax.exam.common.core.base;

public interface RedisKeyConstants {
    /**
     * Redis缓存权限规则key
     */
    String REDIS_ROLES_MAP_KEY = "auth:resourceRolesMap";
    /**
     * 考试答题key
     */
    String REDIS_SUBJECT_MAP_KEY="exam:userIdMap:";
    /**
     * 电子邮件代码key
     */
    String REDIS_EMAIL_CODE_KEY="email:code:";
    /**
     * rds课程班级码缓存  规则：
     */
    String REDIS_COURSE_CLASS_CODE_KEY="exam:course:class_code";

    /**
     * rds在线考试缓存  规则：:${examId}:信息/:${stuId}:作答信息/题目/选项
     */
    String RDS_EXAM_ONLINE_KEY="exam:online:info:";
}
