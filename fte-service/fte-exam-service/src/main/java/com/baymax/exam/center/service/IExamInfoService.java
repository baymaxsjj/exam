package com.baymax.exam.center.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baymax.exam.center.model.ExamInfo;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * <p>
 * 考试信息 服务类
 * </p>
 *
 * @author baymax
 * @since 2022-10-28
 */
public interface IExamInfoService extends IService<ExamInfo> {
    /**
     * 得到学生考试信息
     *
     * @param page 页面
     * @param ew   电子战
     * @return {@link IPage}<{@link ExamInfo}>
     */
    IPage<ExamInfo> getSutExamInfo(IPage<ExamInfo> page, Wrapper ew);
}
