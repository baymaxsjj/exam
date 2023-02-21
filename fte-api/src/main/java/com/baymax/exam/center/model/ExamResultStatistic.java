package com.baymax.exam.center.model;

import com.baymax.exam.center.enums.QuestionTypeEnum;
import com.baymax.exam.user.model.UserAuthInfo;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ：Baymax
 * @date ：Created in 2023/2/21 20:07
 * @description：考试结果统计
 * @modified By：
 * @version:
 */
@Data
public class ExamResultStatistic {
    float averageScore=0F;
    MostScoreInfo maxScoreInfo;
    MostScoreInfo minScoreInfo;
    Map<String ,AnswerStatisticInfo> questionStatistic;
    Map<QuestionTypeEnum,AnswerStatisticInfo> questionTypeStatistic;

    @Data
    public static class MostScoreInfo{
        float score=0;
        UserAuthInfo user;
    }
    @Data
    public static class AnswerStatisticInfo{
        int totalNumber=0;
        int correctNumber=0;

        public void autoAddCorrectNumber(){
            correctNumber++;
        }
        public void autoAddTotalNumber(){
            totalNumber++;
        }

    }
}

