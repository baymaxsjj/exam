package com.baymax.exam.center.config;

/**
 * @author ：Baymax
 * @date ：Created in 2023/2/22 18:29
 * @description：
 * @modified By：
 * @version:
 */
public interface AutoPaperConstants {
    //difficulty_level难度系数取值
    Double DIFFICULTY_LEVEL_VERYEASY=(double) 0.1;
    Double DIFFICULTY_LEVEL_EASY=(double) 0.3;
    Double DIFFICULTY_LEVEL_MEDIUM=(double) 0.5;
    Double DIFFICULTY_LEVEL_HARD=(double) 0.7;
    Double DIFFICULTY_LEVEL_VERYHARD=(double) 0.9;

    double KP_COVERAGE_RATE = 0;//知识点覆盖率 占 适应度 比率
    double DIFFICULTY_RATE = 1;//难度系数  占 适应度比率

    //期望适应度
    double EXPAND_ADATPER=0.95;
    //最大迭代次数
    int RUN_Count=500;
}
