package com.baymax.exam.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baymax.exam.user.model.JoinClass;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baymax.exam.user.model.UserAuthInfo;
import com.baymax.exam.user.model.User;
import com.baymax.exam.user.po.CourseUserPo;

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
    IPage<UserAuthInfo> getClassUsers(Integer classId, long currentPage, long pageSize);

    /**
     * 批处理类用户
     * 得到一批用户
     *
     * @param isInList     是在
     * @param courseUserPo 当然用户订单
     * @param currPage     咕咕叫页面
     * @param pageSize     页面大小
     * @return {@link IPage}<{@link User}>
     */
    IPage<UserAuthInfo> getBatchClassUsers(CourseUserPo courseUserPo, Boolean isInList, long currPage, long pageSize);
}
