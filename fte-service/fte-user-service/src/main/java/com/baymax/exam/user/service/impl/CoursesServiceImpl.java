package com.baymax.exam.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baymax.exam.user.model.Courses;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baymax.exam.user.mapper.CoursesMapper;
import com.baymax.exam.user.service.ICoursesService;
import com.baymax.exam.user.vo.CourseInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 课程信息 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-11
 */
@Service
public class CoursesServiceImpl extends ServiceImpl<CoursesMapper, Courses> implements ICoursesService {
    @Autowired
    CoursesMapper coursesMapper;
    /**
     * 更新课程
     *
     * @param courses 课程
     * @return boolean
     */
    @Override
    public boolean updateCourse(Courses courses) {
        if(courses.getId()==null||courses.getUserId()==null){
            return  false;
        }
        Map<String , Object> queryMap = new HashMap<>();
        queryMap.put("id" , courses.getId());
        queryMap.put("user_id" ,  courses.getUserId());
        return update(courses,new QueryWrapper<Courses>().allEq(queryMap));
    }

    /**
     * 获取课程信息
     *
     * @param id id
     * @return {@link CourseInfoVo}
     */
    @Override
    public CourseInfoVo getCourseInfo(Integer id) {
        return coursesMapper.getCourseInfo(id);
    }


    /**
     * 获得课程列表
     *
     * @param userId      用户id
     * @param currentPage 当前页面
     * @param pageSize    页面大小
     * @param isStudent   是学生?
     * @return {@link IPage}<{@link Courses}>
     */
    @Override
    public IPage<CourseInfoVo> getCourseList(Integer userId,Integer status,Boolean isStudent, long currentPage, long pageSize) {
        Page<CourseInfoVo> page=new Page<>(currentPage,pageSize);
        QueryWrapper<CourseInfoVo> queryWrapper=new QueryWrapper<>();
        Map<String,Object> queryMap=new HashMap<>();
        if(isStudent){
            queryMap.put("student_id",userId);
        }else{
            queryMap.put("user_id",userId);
        }
        queryMap.put("status",status);
        queryWrapper.allEq(queryMap);
        queryWrapper.orderByDesc("created_at");
        return coursesMapper.getCourseList(page,queryWrapper,isStudent);
    }


}
