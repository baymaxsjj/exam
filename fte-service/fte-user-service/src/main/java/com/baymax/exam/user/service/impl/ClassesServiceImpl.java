package com.baymax.exam.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baymax.exam.common.core.base.ExamAuth;
import com.baymax.exam.common.redis.utils.RedisUtil;
import com.baymax.exam.model.Classes;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baymax.exam.user.mapper.ClassesMapper;
import com.baymax.exam.user.service.IClassesService;
import com.baymax.exam.user.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-11
 */
@Service
public class ClassesServiceImpl extends ServiceImpl<ClassesMapper, Classes> implements IClassesService {

    @Autowired
    RedisUtil redisUtil;

    /**
     * 根据课程id获取
     *
     * @param courseId 进程id
     * @return {@link Classes}
     */
    @Override
    public List<Classes> getClassByCourseId(Integer courseId) {
        LambdaQueryWrapper<Classes> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(Classes::getCourseId,courseId);
        return list(queryWrapper);
    }

    /**
     * 生成代码
     *
     * @return {@link String}
     */
    @Override
    public String generateCode(Integer classId) {
        String code = null;
        for(int i=0;i<3;i++){
            code=RandomUtil.generateCode();
            String reCode=getCodeById(classId);
            //班级码有效
            if(reCode==null){
                //有效期7天
                redisUtil.setCacheObject(ExamAuth.REDIS_CLASS_CODE_KEY+classId,code,7, TimeUnit.DAYS);
                //双向映射
                redisUtil.setCacheObject(ExamAuth.REDIS_CODE_CLASS_KEY+code,classId,7,TimeUnit.HOURS);
                break;
            }
        }
        return code;
    }

    /**
     * 班级id获取班级码
     *
     * @param classId 类id
     * @return {@link String}
     */
    @Override
    public String getCodeById(Integer classId) {
        return redisUtil.getCacheObject(ExamAuth.REDIS_CLASS_CODE_KEY+classId);
    }

    /**
     * 班级码获取班级id
     *
     * @param code 代码
     * @return {@link String}
     */
    @Override
    public Integer getClassByCode(String code) {
        return redisUtil.<Integer>getCacheObject(ExamAuth.REDIS_CLASS_CODE_KEY+code);
    }

    /**
     * 有班级码有效时间
     *
     * @param classId
     * @return long
     */
    @Override
    public long getCodeValidTime(Integer classId) {
        return redisUtil.getExpire(ExamAuth.REDIS_CLASS_CODE_KEY+classId);
    }

}
