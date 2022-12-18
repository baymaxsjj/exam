package com.baymax.exam.center.vo;

import com.baymax.exam.center.enums.AnswerStatusEnum;
import com.baymax.exam.center.model.ExamAnswerLog;
import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.user.model.UserAuthInfo;
import lombok.Data;

/**
 * @author ：Baymax
 * @date ：Created in 2022/12/1 20:26
 * @description：学生行为
 * @modified By：
 * @version:
 */
@Data
public class StudentActionVo {
    private UserAuthInfo userAuthInfo;
    private PageResult<ExamAnswerLog> actionPage;
    private AnswerStatusEnum answerStatus;
}
