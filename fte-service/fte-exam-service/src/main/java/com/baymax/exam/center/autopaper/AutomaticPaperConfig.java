package com.baymax.exam.center.autopaper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：Baymax
 * @date ：Created in 2023/3/12 18:23
 * @description：自动组卷配置类
 * @modified By：
 * @version:
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AutomaticPaperConfig {
    private double targetExpand=0.98;
    //定义基本参数
    private int populationSize = 20; //种群规模
    private double crossoverRate = 0.8; //交叉概率
    private double mutationRate = 0.01; //变异概率
    private int maxGeneration = 50; //最大迭代次数
    private boolean elitism=true;//精英主义

    public static double TAG_WEIGHT = 0.20;//知识点权重

    public static double DIFFICULTY_WEIGHT = 0.80;//难度权重
    private int tournamentSize=5;

    public void setSmallScare(){
        targetExpand=0.8;
        populationSize=20;
        maxGeneration=50;
        mutationRate=0.8;
        crossoverRate=0.8;
    }
    public void setMediumScale(){
        targetExpand=0.85;
        populationSize=50;
        maxGeneration=100;
        mutationRate=0.7;
        crossoverRate=0.7;
    }
    public void setLargeScale(){
        targetExpand=0.9;
        populationSize=100;
        maxGeneration=150;
        mutationRate=0.6;
        crossoverRate=0.6;
    }

    /**
     * 根据题量的占比，更改自动组卷的配置，占比越小，迭代次数小，但变异等概率将变大
     *
     * @param desiredNumber  期望数量
     * @param questionNumber 问题数量
     * @return {@link AutomaticPaperConfig}
     */
    public static AutomaticPaperConfig getConfig(int desiredNumber,int questionNumber){
        AutomaticPaperConfig config=new AutomaticPaperConfig();
        //题目占比
        int percentage=questionNumber/desiredNumber*10;
        if(percentage>70){
            config.setSmallScare();
        }else if(percentage>40){
            config.setMediumScale();
        }else{
            config.setLargeScale();
        }
        return config;
    }
}
