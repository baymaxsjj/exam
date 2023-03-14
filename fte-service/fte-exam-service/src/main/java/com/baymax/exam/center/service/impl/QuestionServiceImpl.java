package com.baymax.exam.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baymax.exam.center.enums.OptionStatusEnum;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
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
    public String addQuestion(QuestionInfoVo questionInfo) {
        //1.判断题目是否合法
        QuestionTypeEnum enumByValue =questionInfo.getType();
        log.info(enumByValue.getValue().toString());
        if(enumByValue==null){
            log.info("题目类型不存在");
            return "题目类型不存在";
        }
        int itemMin = enumByValue.getItemMin();
        int itemMax = enumByValue.getItemMax();
        int itemSize = questionInfo.getOptions().size();
        if(itemSize<itemMin||(itemSize>itemMax)){
            log.info("题目项个数不合法");
            return "题目项个数不合法";
        }
        //2.单选，判断，答案只能是一个
        if(enumByValue==QuestionTypeEnum.SIGNAL_CHOICE||enumByValue==QuestionTypeEnum.JUDGMENTAL){
            long count = questionInfo.getOptions().stream().filter(el -> el.getAnswer() != null).count();
            if(count!=1){
                log.info("题目正确选择个数不正确");
                return "题目正确选择个数不正确";
            }
        }
        //3.创建题目
        questionMapper.insert((Question) questionInfo);

        List<QuestionItem> topicItems = questionInfo.getOptions().stream().map(tem->{
            tem.setQuestionId(questionInfo.getId());
            if(enumByValue==QuestionTypeEnum.SIGNAL_CHOICE||enumByValue==QuestionTypeEnum.JUDGMENTAL||enumByValue==QuestionTypeEnum.MULTIPLE_CHOICE){
                if(!ObjectUtils.isEmpty(tem.getAnswer())){
                    tem.setAnswer(OptionStatusEnum.SELECT.value);
                }
            }
            return tem;
        }).collect(Collectors.toList());
        //4.创建选项
        questionItemService.saveBatch(topicItems);
        return "";
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

    @Override
    public List<Question> getQuestionsByTags(int userId,int courseId, Collection<Integer> tagList, Collection<QuestionTypeEnum> typeList) {
        LambdaQueryWrapper<Question> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getTeacherId,userId);
        queryWrapper.eq(Question::getCourseId,courseId);
        if(tagList!=null&&!tagList.isEmpty()){
            if(tagList.contains(0)){
                queryWrapper.or().isNotNull(Question::getType);
                tagList.remove(0);
            }
            queryWrapper.in(Question::getTagId,tagList);
        }
        if(typeList!=null&&!typeList.isEmpty()){
            queryWrapper.in(Question::getType,typeList);
        }
        return list(queryWrapper);
    }

    public List<Question> examQuestion(Integer examPaperId) {
        return questionMapper.examQuestion(examPaperId);
    }
}
