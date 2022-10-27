package com.baymax.exam.center.vo;

import com.baymax.exam.center.model.Exam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
    Exam exam;
    Set<Integer> questions;
}
