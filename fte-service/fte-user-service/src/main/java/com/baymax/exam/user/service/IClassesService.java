package com.baymax.exam.user.service;

import com.baymax.exam.user.model.Classes;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baymax
 * @since 2022-10-11
 */
public interface IClassesService extends IService<Classes> {

    /**
     * 根据课程id获取
     *
     * @param courseId 进程id
     * @return {@link Classes}
     */
    List<Classes> getClassByCourseId(Integer courseId);
    /**
     * 生成代码
     *
     * @param classId 类id
     * @return {@link String}
     */
    public String generateCode(Integer classId);

    /**
     * 班级id获取班级码
     *
     * @param classId 类id
     * @return {@link String}
     */
    public String getCodeById(Integer classId);

    /**
     * 班级码获取班级id
     *
     * @param code 代码
     * @return {@link String}
     */
    public Integer getClassByCode(String code);

    /**
     * 有班级码有效时间
     *
     * @return long
     */
    public long getCodeValidTime(Integer classId);
}
