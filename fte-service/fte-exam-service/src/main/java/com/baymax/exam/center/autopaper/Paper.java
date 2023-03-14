package com.baymax.exam.center.autopaper;

import com.baymax.exam.center.model.Question;
import com.baymax.exam.center.vo.AutomaticPaperRuleVo;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：Baymax
 * @date ：Created in 2023/3/11 10:15
 * @description：试卷个体
 * @modified By：
 * @version:
 */
@Data
public class Paper {
    private  int id;
    /**
     * 适应度
     */
    private double adaptationDegree = 0;
    /**
     * 知识点覆盖率
     */
    private double tagCoverage = 0;
    /**
     * 试卷总分
     */
    private double totalScore = 0;
    /**
     * 试卷难度系数
     */
    private double difficulty = 0;
    /**
     * 个体包含的试题集合
     */
    private List<Question> questionList = new LinkedList<>();

    public double getTotalScore() {
        if(totalScore==0){
            totalScore=questionList.stream().mapToDouble(Question::getScore).sum();
        }
        return totalScore;
    }

    public double getDifficulty() {
        if(difficulty==0){
            difficulty=questionList.stream().mapToDouble(question->question.getScore()*question.getDifficulty()).sum()/getTotalScore();
        }
        return difficulty;
    }

    public void setTagCoverage(Set<Integer> tags) {
        if(tagCoverage==0&&!tags.isEmpty()){
            Set<Integer> result = new HashSet<>(tags);
            Set<Integer> another = questionList.stream().map(Question::getTagId).collect(Collectors.toSet());
            // 交集操作
            result.retainAll(another);
            tagCoverage =(double) result.size() / tags.size();
        }else{
            tagCoverage=0.5;
        }
    }

    public void setAdaptationDegree(AutomaticPaperRuleVo rule, double f1, double f2) {
        if (adaptationDegree == 0) {
            adaptationDegree = 1 - (1 - getTagCoverage()) * f1 - Math.abs(rule.getDifficulty() - getDifficulty()) * f2;
        }
    }
    public boolean containsQuestion(Question question) {
        return questionList.stream().anyMatch(q-> q==null || Objects.equals(question.getId(), q.getId()));
    }

    /**
     * 增加问题
     *
     * @param question
     */
    public void saveQuestion(int index, Question question) {
        questionList.set(index, question);
        clearDataCache();
    }

    /**
     * 添加问题
     *
     * @param question 问题
     */
    public void addQuestion(Question question) {
        questionList.add(question);
        clearDataCache();
    }

    /**
     * 获取题目
     *
     * @param index 指数
     * @return {@link Question}
     */
    public Question getQuestion(int index) {
        return questionList.get(index);
    }

    /**
     * 得到问题大小
     *
     * @return int
     */
    public int getQuestionSize() {
        return questionList.size();
    }

    /**
     * 清除数据缓存
     */
    public void clearDataCache(){
        totalScore = 0;
        adaptationDegree = 0;
        difficulty = 0;
        tagCoverage = 0;
    }
}
