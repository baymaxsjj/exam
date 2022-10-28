package com.baymax.exam.center.vo;

import com.baymax.exam.center.model.ExamClass;
import com.baymax.exam.center.model.ExamInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/28 10:49
 * @description：考试信息
 * @modified By：
 * @version:
 */
@Data
@Schema(name = "ExamInfoVo", description = "考试信息")
public class ExamInfoVo {
    @Valid
    @NotNull(message = "考试信息不能为空")
    private ExamInfo examInfo;

    @NotNull(message = "班级列表不能为空")
    private Set<Integer> classList;
}
