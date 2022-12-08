package com.baymax.exam.user.utils;

import static com.baymax.exam.common.core.base.RedisKeyConstants.REDIS_COURSE_CLASS_CODE_KEY;

/**
 * @author ：Baymax
 * @date ：Created in 2022/12/7 18:50
 * @description：班级码reids规则
 * @modified By：
 * @version:
 */
public class ClassCodeRedisKeys {
    public static String getClassKey(int classId){
        return baseKey("class_List")+":"+classId;
    }
    public static String getCodeKey(String code){
        return baseKey("code_list")+":"+code;
    }
    public static String getCodeListKey(){
        return baseKey("codes");
    }
    public static String getGenerateFlagKey(){
        return baseKey("generate_flag");
    }
    public static String getGroupValueKey(){
        return baseKey("group_value");
    }
    private static String baseKey(String key){
        return REDIS_COURSE_CLASS_CODE_KEY+":"+key;
    }
}
