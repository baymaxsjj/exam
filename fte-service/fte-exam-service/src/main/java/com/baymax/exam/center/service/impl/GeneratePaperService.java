package com.baymax.exam.center.service.impl;

import com.baymax.exam.center.autopaper.AutomaticPaperConfig;
import com.baymax.exam.center.autopaper.Paper;
import com.baymax.exam.center.autopaper.Population;
import com.baymax.exam.center.enums.QuestionTypeEnum;
import com.baymax.exam.center.model.Question;
import com.baymax.exam.center.vo.AutomaticPaperRuleVo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ：Baymax
 * @date ：Created in 2023/3/11 15:44
 * @description：
 * @modified By：
 * @version:
 */
public class GeneratePaperService {
    private List<Question> questions;
    private AutomaticPaperRuleVo rule;

    private AutomaticPaperConfig config;

    /**
     * 生成论文服务
     *
     * @param questions 问题
     * @param rule      规则
     */
    public GeneratePaperService(List<Question> questions, AutomaticPaperRuleVo rule){
        this.questions=questions;
        this.rule=rule;
        config=AutomaticPaperConfig.getConfig(rule.getTotalNumber(),questions.size());
    }

    public Paper generatePaper() {
        Population population = new Population(config.getPopulationSize(),questions, rule);
        // 迭代计数器
        int count = 0;
        Paper bastPaper;
        do{
            count++;
            population = evolvePopulation(population);
            bastPaper=population.getFitness();
        }while (count < config.getMaxGeneration() && bastPaper.getAdaptationDegree() < config.getTargetExpand());
        return population.getFitness();
    }

    private Population evolvePopulation(Population population) {
        //新总群
        Population nextPopulation=new Population(config.getPopulationSize());
        if(config.isElitism()){
            nextPopulation.addPaper(population.getFitness());
        }
        //交叉
        int initIndex=nextPopulation.getPaperIndex();
        for(;initIndex<config.getPopulationSize();initIndex++){
            // 较优选择parent
            Paper parent1 = select(population);
            Paper parent2 = select(population);
            while (parent2.getId() == parent1.getId()) {
                parent2 = select(population);
            }
            // 交叉
            Paper child = crossover(parent1, parent2);
            nextPopulation.addPaper(child);
        }
        initIndex=nextPopulation.getPaperIndex();
        // 种群变异操作
        Paper tmpPaper;
        for (; initIndex<config.getPopulationSize();initIndex++) {
            tmpPaper = nextPopulation.getPaper(initIndex);
            mutate(tmpPaper);
            // 计算知识点覆盖率与适应度
            tmpPaper.setTagCoverage(rule.getTags());
            tmpPaper.setAdaptationDegree(rule, AutomaticPaperConfig.TAG_WEIGHT, AutomaticPaperConfig.DIFFICULTY_WEIGHT);
        }
        return nextPopulation;
    }

    /**
     *选择操作：根据适应度值，采用某种选择策略（如轮盘赌法、锦标赛法等），从当前种群中选择一定数量的个体进入下一代。
     * @param population 种群
     * @return {@link Paper}
     */
    private Paper select(Population population) {
        return population.getChildPopulation(config.getTournamentSize()).getFitness();
    }

    /**
     * 交叉操作：根据交叉概率，在选择出来的个体中随机配对，并在某一位置进行基因交换，产生新的子代。
     *
     * @param currentPaper 当前纸
     * @param nextPaper    下一个纸
     * @return {@link Paper}
     */
    private Paper crossover(Paper currentPaper, Paper nextPaper) {
        Paper paper=new Paper();
        int retainNumber= (int) (config.getCrossoverRate()*currentPaper.getQuestionSize());
        //保留个体
        int index;
        for(index=0;index<retainNumber;index++){
            paper.saveQuestion(index,currentPaper.getQuestion(index));
        }
        //个体交叉
        for(index=retainNumber;index<nextPaper.getQuestionSize();index++){
            int temIndex=index;
            Question question=nextPaper.getQuestion(index);
            if(paper.containsQuestion(question)){
                question=getRandomQuestion();
                //还是有一定的概率，会重的，所以下标要变
                if(paper.containsQuestion(question)){
                    index--;
                }
            }
            paper.saveQuestion(temIndex,question);
        }
        return paper;
    }

    /**
     * 变异
     *变异操作：根据变异概率，在子代中随机选取若干位进行基因翻转，产生新的变异子代。
     * @param paper 试卷
     */
    private void mutate(Paper paper) {
        for (int i = 0; i < paper.getQuestionList().size(); i++) {
            if(Math.random()<config.getMutationRate()){
                paper.saveQuestion(i,getRandomQuestion());
            }
        }
    }
    private Question getRandomQuestion(){
        int randomIndex= (int) (Math.random()*questions.size());
        return questions.get(randomIndex);
    }
}
