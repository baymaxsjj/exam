package com.baymax.exam.center.service;

import com.baymax.exam.center.enums.ExamAnswerLogEnum;
import com.baymax.exam.center.model.ExamAnswerLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baymax.exam.center.model.ExamInfo;

/**
 * <p>
 * 考试作答日志 服务类
 * </p>
 *
 * @author baymax
 * @since 2022-11-04
 */
public interface IExamAnswerLogService extends IService<ExamAnswerLog> {
    /**
     * 写日志
     *
     * @param stuId    斯图id
     * @param logEnum  日志枚举
     * @param info     信息
     * @param classId  类id
     * @param examInfo 考试信息
     */
    public void writeLog(Integer stuId,Integer classId, ExamInfo examInfo, ExamAnswerLogEnum logEnum, String info) ;
}
