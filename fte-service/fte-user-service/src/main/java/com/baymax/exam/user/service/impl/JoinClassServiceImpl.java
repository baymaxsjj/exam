package com.baymax.exam.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baymax.exam.user.model.JoinClass;
import com.baymax.exam.user.model.User;
import com.baymax.exam.user.mapper.JoinClassMapper;
import com.baymax.exam.user.po.CourseUserPo;
import com.baymax.exam.user.service.IJoinClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-13
 */
@Slf4j
@Service
public class JoinClassServiceImpl extends ServiceImpl<JoinClassMapper, JoinClass> implements IJoinClassService {
    @Autowired
    JoinClassMapper joinClassMapper;
    /**
     * 获取是否加入班级
     *
     * @param userId  用户id
     * @param classId 班级id
     * @return {@link JoinClass}
     */
    @Override
    public JoinClass getJoinByClassId(Integer userId, Integer classId) {
        QueryWrapper<JoinClass> queryWrapper=new QueryWrapper<>();
        Map<String,Object> map=new HashMap();
        map.put("student_id",userId);
        map.put("class_id",classId);
        queryWrapper.allEq(map);
        return getOne(queryWrapper);
    }

    /**
     * 课程id获取加入信息
     *
     * @param userId   用户id
     * @param courseId 进程id
     * @return {@link JoinClass}
     */
    @Override
    public JoinClass getJoinByCourseId(Integer userId, Integer courseId) {
        return joinClassMapper.getJoinByCourseId(courseId,userId);
    }

    /**
     * 获取班级用户
     *
     * @param classId 班级id
     * @param currentPage 当前页面
     * @param pageSize    页面大小
     * @return {@link IPage}<{@link User}>
     */
    @Override
    public IPage<User> getClassUsers(Integer classId,long currentPage,long pageSize) {
        Page<User> page=new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<JoinClass> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(JoinClass::getClassId,classId);
        return joinClassMapper.getJoinClassUser(page,queryWrapper);
    }

    /**
     * 批处理类用户
     * 得到一批用户
     *
     * @param pageSize     页面大小
     * @param isInList     在列表
     * @param courseUserPo 当然用户订单
     * @param currPage     咕咕叫页面
     * @return {@link IPage}<{@link User}>
     */
    @Override
    public IPage<User> getBatchClassUsers(CourseUserPo courseUserPo, Boolean isInList,long currPage,long pageSize) {
        Page<User> page=new Page<>(currPage,pageSize);
        QueryWrapper queryWrapper=new QueryWrapper();
        log.info("getBatchClassUsers:{}",courseUserPo);
        queryWrapper.in("jc.class_id",courseUserPo.getClassIds());
        if(courseUserPo.getStudentIds()!=null){
            //使用in/not in的时候，不能为空
            boolean empty = courseUserPo.getStudentIds().isEmpty();
            if(empty&&isInList){
                return new Page<>();
            }else if(!empty){
                if(isInList){
                    queryWrapper.in("u.id",courseUserPo.getStudentIds());
                }else{
                    queryWrapper.notIn("u.id",courseUserPo.getStudentIds());
                }
            }
        }

        return joinClassMapper.getJoinClassUser(page,queryWrapper);
    }
}
