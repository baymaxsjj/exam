package com.baymax.exam.center.autopaper;

import com.baymax.exam.center.enums.QuestionTypeEnum;
import com.baymax.exam.center.model.Question;
import com.baymax.exam.center.vo.AutomaticPaperRuleVo;
import lombok.Data;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author ：Baymax
 * @date ：Created in 2023/3/11 10:14
 * @description：种群
 * @modified By：
 * @version:
 */
@Data
public class Population {

    Paper[] papers;
    private int paperIndex=0;
    public Population(int populationSize, List<Question> questionList, AutomaticPaperRuleVo rule) {
        Collections.shuffle(questionList);
        Map<QuestionTypeEnum,List<Question>> questionGroups=questionList.stream().collect(Collectors.groupingBy(Question::getType));
        Paper paper;
        for (int i = 0; i < populationSize; i++) {
            paper = new Paper();
            paper.setId(i + 1);
            //去除总分限制，
//           while (paper.getTotalScore()>=rule.getTotalScore()){
            paper.getQuestionList().clear();
            //获取题目
            Paper finalPaper = paper;
            //FIXME:题型占比，如果所以题型都不够就组卷失败，如果只给题型，没有占比，要如何处理？
            if(rule.getQuestionType()!=null){
                //1.判断题库中题型占比
                Map<QuestionTypeEnum,Integer> typeNumberList=new HashMap<>();
                AtomicReference<Float> surplus= new AtomicReference<>((float) 0);
                questionGroups.forEach((type,list)->{
                    int number=0;
                    float percent=surplus.get()+rule.getPercentage().getOrDefault(type,(float)list.size()/questionList.size());
                    int retainNumber= (int) (rule.getTotalNumber()*percent);
                    //2.对期望类型占比，对不满足的进行重新分配
                    if(retainNumber>list.size()){
                        number= list.size();
                        //将不够的份额，给下一个类型；
                        surplus.set(percent-rule.getTotalNumber() / list.size());
                    }else{
                        surplus.set(0f);
                    }
                    //3.适当修改百分比，保证题目总数达到预期
                    typeNumberList.put(type, number);
                });
                //要保证题型占比和
                rule.getQuestionType().forEach((type)->{
                    //添加题目
                    List<Question> list=questionGroups.get(type).stream().limit(typeNumberList.get(type)).toList();
                    finalPaper.getQuestionList().addAll(list);
                });
            }else{
                for (int j=0;j<rule.getTotalNumber();j++){
                    finalPaper.saveQuestion(j,questionList.get(j));
                }
            }
            // 计算试卷知识点覆盖率
            finalPaper.setTagCoverage(rule.getTags());
            // 计算试卷适应度
            finalPaper.setAdaptationDegree(rule, AutomaticPaperConfig.TAG_WEIGHT,AutomaticPaperConfig.DIFFICULTY_WEIGHT);
            papers[i] = finalPaper;
        }
    }
    public Population(int populationSize) {
        papers = new Paper[populationSize];
    }
    public Population(Paper[] list) {
        papers = list;
        paperIndex=list.length-1;
    }

    /**
     * 获取种群中最优秀个体
     *
     * @return {@link Paper}
     */
    public Paper getFitness() {
        return Arrays.stream(papers).max(Comparator.comparing(Paper::getAdaptationDegree)).get();
    }

    public Population getChildPopulation(int number){
        //乱序
        List<Paper> list=Arrays.stream(papers).toList();
        Collections.shuffle(list);
        //选number
        Paper[] childPapers= (Paper[]) list.stream().limit(number).toArray();
        return new Population(childPapers);
    }


    /**
     * 获取种群中某个个体
     *
     * @param index
     * @return
     */
    public Paper getPaper(int index) {
        return papers[index];
    }

    /**
     * 设置种群中某个个体
     *
     * @param paper
     */
    public void addPaper(Paper paper) {
        if(paperIndex<papers.length){
            papers[paperIndex++] = paper;
        }
    }

    /**
     * 返回种群规模
     *
     * @return
     */
    public int getLength() {
        return papers.length;
    }


}
