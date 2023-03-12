package com.baymax.exam.center.vo;

import com.baymax.exam.center.enums.QuestionTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author ：Baymax
 * @date ：Created in 2023/3/10 18:32
 * @description：自动组卷参数
 * @modified By：
 * @version:
 */
@Data
public class AutomaticPaperVo {

    @Schema(defaultValue = "题目类型")
    private Set<QuestionTypeEnum> questionTypes;
    @Schema(defaultValue = "题目难度")
    private Byte difficulty;
    @Schema(defaultValue = "题目标签")
    private List<Integer> tags;
    @Schema(defaultValue = "题目数量")
    private Integer totalNumber;
    @Schema(defaultValue = "题目总分吧")
    private Integer totalScore;

}
