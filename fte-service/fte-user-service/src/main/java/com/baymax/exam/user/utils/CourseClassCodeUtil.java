package com.baymax.exam.user.utils;

import com.baymax.exam.common.redis.utils.RedisLockUtils;
import com.baymax.exam.common.redis.utils.RedisUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author ：Baymax
 * @date ：Created in 2022/12/7 18:34
 * @description：课程码工具
 * @modified By：
 * @version:
 * 待解决：一致性
 *      解决方法：设置标志，班级码分段生成。生成的班级码，队列出栈
 * redis固定key
 *      class
 *          id:code
 *      code
 *          code:id
 *      codeMap
 *          code:过期时间
 *
 */
@Slf4j
@Service
public class CourseClassCodeUtil {
    private static int classCodeTimeOut=7;
    private static TimeUnit timeOutAccuracy=TimeUnit.DAYS;
    //每次生成多少个
    private static int generateCodeCount=1000;
    //可用码的最小数，
    private static int availableCodeMin=200;
    //班级码长度
    private static int codeLength=6;
    //分组
    private static int codeGroupCount=100;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    RedisLockUtils redisLockUtils;
    @Autowired
    public RedisTemplate redisTemplate;

    public String getClassCode(int classId,boolean isNew){
        //1。判断是否存在班级码
        String classKey = ClassCodeRedisKeys.getClassKey(classId);
        boolean hasKey = redisUtils.hasKey(classKey);
        if(!isNew&&hasKey){
            return redisUtils.getCacheObject(classKey);
        }
        //2.没有班级码，获取一个可用的班级码
        String availableCode = getAvailableCode();
        String codeKey = ClassCodeRedisKeys.getCodeKey(availableCode);
        //班级id:班级码互相映射
        redisUtils.setCacheObject(classKey,availableCode,classCodeTimeOut,timeOutAccuracy);
        redisUtils.setCacheObject(codeKey,classId,classCodeTimeOut,timeOutAccuracy);
        return availableCode;
    }
    public Integer getClassIdByCode(String code){
        String codeKey = ClassCodeRedisKeys.getCodeKey(code);
        return redisUtils.getCacheObject(codeKey);
    }
    public LocalDateTime getExtricableTime(String code){
        String codeKey = ClassCodeRedisKeys.getCodeKey(code);
        long expire = redisUtils.getExpire(codeKey);
        return LocalDateTime.now().plusSeconds(expire);
    }
    public String getAvailableCode(){
        String codeListKey = ClassCodeRedisKeys.getCodeListKey();
        //从队列取出一个
        if(!redisUtils.hasKey(codeListKey)||redisTemplate.opsForSet().size(codeListKey)<availableCodeMin){
            saveClassCode();
        }
        Object pop = redisTemplate.opsForSet().pop(codeListKey);
        return pop.toString();
    }
    private Set<String> batchRandomCode(){
        //从一个起始值生成，随机数
        Set<String> list=new HashSet<>();
        //生成一批数，添加到队列中，并保存当前的范围
        //10000000-99999999
        //每组生成几个码
        int groupRunCount=generateCodeCount/codeGroupCount;
        //每组最大码
        String groupValueKey = ClassCodeRedisKeys.getGroupValueKey();
        Map<String, CodeGroup> cacheMap;
        if(redisUtils.hasKey(groupValueKey)){
            cacheMap = redisUtils.getCacheMap(groupValueKey);
        }else{
            int averageGroupCount=(int)(Math.pow(10,codeLength)-Math.pow(10,codeLength-1))/codeGroupCount;
            log.info("每组最多：{}",averageGroupCount);
            int minValue= (int) Math.pow(10,codeLength-1);
            log.info("最小的码：{}",minValue);
            cacheMap=new HashMap<>();
            //系统首次运行
            for(int i=0;i<codeGroupCount;i++){
                CodeGroup codeGroup=new CodeGroup();
                int min=minValue+averageGroupCount*i;
                int max=min+averageGroupCount;
                codeGroup.setMaxValue(max);
                codeGroup.setMinValue(min);
                codeGroup.initValue();
                cacheMap.put(String.valueOf(i),codeGroup);
            }
            log.info("分组信息：{}",cacheMap);
        }
        //分组
        for(int i=0;i<codeGroupCount;i++){
            //获取每组之前生成的最大值
            CodeGroup codeGroup = cacheMap.get(String.valueOf(i));
            for(int j=0;j<groupRunCount;j++){
                int nextValue = codeGroup.getNextValue();
                //短时效的情况下，码的长度足够，生成码可以不连续，反正够用
                list.add(String.valueOf(nextValue));
            }
            //保存当前组的最大值
            cacheMap.put(String.valueOf(i),codeGroup);
        }
        log.info(list.toString());
        //保存分组信息
        redisUtils.setCacheMap(groupValueKey,cacheMap);
        return list;
    }
    public void saveClassCode(){
        String codeListKey = ClassCodeRedisKeys.getCodeListKey();
        String generateFlagKey = ClassCodeRedisKeys.getGenerateFlagKey();
        Boolean lock = redisLockUtils.getLock(generateFlagKey, "0");
        if(lock){
            Set<String> list = batchRandomCode();
            redisUtils.setCacheSet(codeListKey,list);
            redisLockUtils.releaseLock(generateFlagKey,"0");
        }
    }
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class CodeGroup implements Serializable {
    private int minValue;
    private int maxValue;
    private int boundaryValue;

    /**
     *  初始化边界值
     */
    public int initValue(){
        boundaryValue=minValue;
        return boundaryValue;
    }

    /**
     * 获取下一个边界值
     * @return {@link String}
     */
    public int getNextValue(){
        //每次生成的码尽量分散，但同时也要保证，下次生成不能和之前的冲突
        boundaryValue += (int)(Math.random() * 10);
        //每个组的达到上限后，从头开始
        if(boundaryValue>maxValue){
            return initValue();
        }
        return boundaryValue;
    }
}
