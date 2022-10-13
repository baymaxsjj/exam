package com.baymax.exam.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baymax.exam.model.Courses;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baymax.exam.user.service.impl.CoursesServiceImpl;
import com.baymax.exam.vo.CourseInfoVo;

/**
 * <p>
 * 课程信息 服务类
 * </p>
 *
 * @author baymax
 * @since 2022-10-11
 */
public interface ICoursesService extends IService<Courses> {
    /**
     * 更新课程
     *
     * @param courses 课程
     * @return boolean
     */
    boolean updateCourse(Courses courses);

    /**
     * 得到课程
     *
     * @param id     id
     * @param userId 用户id
     * @return {@link Courses}
     */
    Courses getCourse(Integer id,Integer userId);

    /**
     * 获得课程列表
     *
     * @param userId      用户id
     * @param isStudent   是学生?
     * @param pageSize    页面大小
     * @param currentPage 当前页面
     * @param status      状态
     * @return {@link IPage}<{@link CourseInfoVo}>
     */
    public IPage<CourseInfoVo> getCourseList(Integer userId,Integer status,Boolean isStudent, long currentPage, long pageSize);
}
