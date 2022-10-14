package com.baymax.exam.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baymax.exam.model.JoinClass;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baymax.exam.model.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baymax
 * @since 2022-10-13
 */
public interface IJoinClassService extends IService<JoinClass> {
    /**
     * 班级id获取加入信息
     *
     * @param userId  用户id
     * @param classId 班级id
     * @return {@link JoinClass}
     */
    JoinClass getJoinByClassId(Integer userId,Integer classId);

    /**
     * 课程id获取加入信息
     *
     * @param userId   用户id
     * @param courseId 进程id
     * @return {@link JoinClass}
     */
    JoinClass getJoinByCourseId(Integer userId,Integer courseId);

    /**
     * 得到类用户
     * 获取班级用户
     *
     * @param classId     班级id
     * @param currentPage 当前页面
     * @param pageSize    页面大小
     * @return {@link IPage}<{@link User}>
     */
    IPage<User> getClassUsers(Integer classId,long currentPage,long pageSize);
}
