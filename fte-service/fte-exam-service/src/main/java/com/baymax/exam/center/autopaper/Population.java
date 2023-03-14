package com.baymax.exam.center.autopaper;

import com.baymax.exam.center.enums.QuestionTypeEnum;
import com.baymax.exam.center.model.Question;
import com.baymax.exam.center.vo.AutomaticPaperRuleVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@Data
public class Population {

    Paper[] papers;
    private int paperIndex=0;
    public Population(int populationSize, List<Question> questionList, AutomaticPaperRuleVo rule) {
        Collections.shuffle(questionList);
        Map<QuestionTypeEnum,List<Question>> questionGroups=questionList.stream().collect(Collectors.groupingBy(Question::getType));
        papers = new Paper[populationSize];
        Paper paper;
        for (int i = 0; i < populationSize; i++) {
            paper = new Paper();
            paper.setId(i + 1);
            //去除总分限制，
//           while (paper.getTotalScore()>=rule.getTotalScore()){
            paper.getQuestionList().clear();
            //获取题目
            Paper finalPaper = paper;
            List<QuestionTypeEnum> questionType = rule.getQuestionType();
            //此组卷方式有很大几率失败，如果要对占比重新分配
            if(questionType!=null&&!questionType.isEmpty()){
                Map<QuestionTypeEnum, Float> percentage = new HashMap<>(rule.getPercentage());
                //没分配就按题库题型实际占比
                if(rule.getPercentage()==null||rule.getPercentage().isEmpty()){
                    for (QuestionTypeEnum key:questionGroups.keySet()) {
                        percentage.put(key,(float)questionGroups.get(key).size()/questionList.size());
                    }
                }
                //预分配题型
                int surplusNumber=rule.getTotalNumber();
                List<Question> list=new ArrayList<>();
                Set<QuestionTypeEnum> surplusKeys = new HashSet<>(percentage.keySet());
                Map<QuestionTypeEnum,Integer> assignmentNumber=new HashMap<>();
                for (QuestionTypeEnum key:percentage.keySet()) {
                    float percent=percentage.get(key);
                    //预期题数
                    int expectNumber= (int) (percent*rule.getTotalNumber());
                    //试卷题数
                    int  actualNumber=0;
                    if(questionGroups.get(key)!=null){
                        actualNumber=questionGroups.get(key).size();
                    }
                    //不够分配
                    if(expectNumber>actualNumber){
                        surplusKeys.remove(key);
                        assignmentNumber.put(key,actualNumber);
                        surplusNumber-=actualNumber;
                    }else{
                        assignmentNumber.put(key,expectNumber);
                        surplusNumber-=expectNumber;
                    }
                }
                //对充足的题型，进行平均分配
                Iterator<QuestionTypeEnum> iterator;
                QuestionTypeEnum key;
                int  actualNumber,realNumber;
                while (surplusNumber>0){
                    iterator = surplusKeys.iterator();
                    while (iterator.hasNext()){
                        key = iterator.next();
                        actualNumber=questionGroups.get(key).size();
                        realNumber=assignmentNumber.get(key);
                        if(realNumber<actualNumber){
                            surplusNumber--;
                            assignmentNumber.put(key,++realNumber);
                        }else {
                            iterator.remove();
                        }
                        if(surplusNumber==0){
                            break;
                        }
                    }
                }
                assignmentNumber.forEach((type,number)->{
                    if(number>0){
                        finalPaper.getQuestionList().addAll(questionGroups.get(type).stream().limit(number).toList());
                    }
                });
            }else{
                for (int j=0;j<rule.getTotalNumber();j++){
                    finalPaper.addQuestion(questionList.get(j));
                }
            }
            // 计算试卷知识点覆盖率
            finalPaper.setTagCoverage(rule.getTags());
            // 计算试卷适应度
            finalPaper.setAdaptationDegree(rule, AutomaticPaperConfig.TAG_WEIGHT,AutomaticPaperConfig.DIFFICULTY_WEIGHT);
            papers[i] = finalPaper;
        }
        log.info("种群信息：{}",papers);
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
        List<Paper> list=new ArrayList<>(Arrays.asList(papers));
        Collections.shuffle(list);
        list=list.stream().limit(number).toList();
        //选number
        Paper[] childPapers= list.toArray(new Paper[number]);
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
