package com.baymax.exam.center.vo;

import com.baymax.exam.center.model.Question;
import com.baymax.exam.center.model.QuestionItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/18 14:43
 * @description：题目信息
 * @modified By：
 * @version:
 */
@Data
@Schema(name = "QuestionInfoVo", description = "题目具体信息")
public class QuestionInfoVo extends Question {
    private List<QuestionItem> topicItems;
}
