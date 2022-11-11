package com.baymax.exam.center.vo;

import com.baymax.exam.center.model.ExamPaper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/27 17:40
 * @description：
 * @modified By：
 * @version:
 */
@Data
@Schema(name = "ExamPaperVo", description = "试卷题目具体信息")
public class ExamPaperVo {
    @Valid
    @NotNull(message = "试卷信息不能为空")
    ExamPaper examPaper;
    Set<Integer> questions;
}
