package com.baymax.exam.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baymax.exam.center.enums.QuestionTypeEnum;
import com.baymax.exam.center.mapper.QuestionItemMapper;
import com.baymax.exam.center.model.Question;
import com.baymax.exam.center.mapper.QuestionMapper;
import com.baymax.exam.center.model.QuestionItem;
import com.baymax.exam.center.service.IQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baymax.exam.center.vo.QuestionInfoVo;
import com.baymax.exam.common.core.base.IBaseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 题目信息 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-18
 */
@Slf4j
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    QuestionItemServiceImpl questionItemService;
    /**
     * 添加题目
     *
     * @param questionInfo 问题信息
     * @return boolean
     */
    @Override
    public boolean addQuestion(QuestionInfoVo questionInfo) {
        //1.判断题目是否合法
        QuestionTypeEnum enumByValue =questionInfo.getType();
        log.info(enumByValue.getValue().toString());
        if(enumByValue==null){
            log.info("题目类型不存在");
            return false;
        }
        int itemMin = enumByValue.getItemMin();
        int itemMax = enumByValue.getItemMax();
        int itemSize = questionInfo.getTopicItems().size();
        if(itemSize<itemMin||(itemSize>itemMax&&itemMax>=0)){
            log.info("题目项个数不合法");
            return false;
        }
        //2.单选，判断，答案只能是一个
        if(enumByValue==QuestionTypeEnum.SIGNAL_CHOICE||enumByValue==QuestionTypeEnum.JUDGMENTAL){
            long count = questionInfo.getTopicItems().stream().filter(el -> el.getCorrect() != null).count();
            if(count!=1){
                log.info("题目正确选择个数不正确");
                return false;
            }
        }
        //3.创建题目
        questionMapper.insert((Question) questionInfo);
        List<QuestionItem> topicItems = questionInfo.getTopicItems().stream().map(tem->{
            tem.setQuestionId(questionInfo.getId());
            //填空题选择 就是答案
            if(enumByValue==QuestionTypeEnum.COMPLETION){
                tem.setCorrect("1");
            }
            return tem;
        }).collect(Collectors.toList());
        //4.创建选项
        questionItemService.saveBatch(topicItems);
        return true;
    }

    /**
     * 问题信息
     *
     * @param questionId 问题id
     * @return {@link QuestionInfoVo}
     */
    @Override
    public QuestionInfoVo questionInfo(Integer questionId) {
        return questionMapper.questionInfo(questionId);
    }

    @Override
    public List<QuestionInfoVo> examQuestionInfo(Integer examId) {
        return questionMapper.examQuestionInfo(examId);
    }
}
