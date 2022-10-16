package com.baymax.exam.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baymax.exam.model.JoinClass;
import com.baymax.exam.model.User;
import com.baymax.exam.user.mapper.JoinClassMapper;
import com.baymax.exam.user.service.IJoinClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baymax.exam.vo.CourseInfoVo;
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
}
