package com.baymax.exam.center.service;

import com.baymax.exam.center.model.ExamClass;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baymax
 * @since 2022-10-28
 */
public interface IExamClassService extends IService<ExamClass> {
    /**
     * 删除考试班级
     *
     * @param classId    班级id
     * @param examInfoId 考试信息id
     * @return {@link Integer}
     */
    Integer delExamClass(int classId,int examInfoId);
}
