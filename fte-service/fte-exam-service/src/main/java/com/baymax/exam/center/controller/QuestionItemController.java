package com.baymax.exam.center.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baymax.exam.center.enums.QuestionTypeEnum;
import com.baymax.exam.center.mapper.QuestionItemMapper;
import com.baymax.exam.center.model.Question;
import com.baymax.exam.center.model.QuestionItem;
import com.baymax.exam.center.service.impl.QuestionItemServiceImpl;
import com.baymax.exam.center.service.impl.QuestionServiceImpl;
import com.baymax.exam.common.core.base.IBaseEnum;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 题目选择表 前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-10-18
 */
@Slf4j
@Tag(name = "题目选项")
@RestController
@Validated
@RequestMapping("/question-item")
public class QuestionItemController {
    @Autowired
    QuestionItemServiceImpl questionItemService;
    @Autowired
    QuestionServiceImpl questionService;
    @Autowired
    QuestionItemMapper questionItemMapper;
    @Operation(summary = "删除选项")
    @PostMapping("/delete/{itemId}")
    public Result delete(@PathVariable String itemId){
        QuestionItem item = questionItemService.getById(itemId);
        if(item==null){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        Question question = questionService.getById(item.getQuestionId());
        if(question.getTeacherId()!=UserAuthUtil.getUserId()){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //判断删除数目,是否符合标准
        QuestionTypeEnum type = question.getType();
        int itemMin = type.getItemMin();
        long count = questionItemService.count(new LambdaQueryWrapper<QuestionItem>().eq(QuestionItem::getQuestionId, question.getId()));
        if(count<=itemMin){
            return Result.msgError("删除失败，达到选项个数最小限制");
        }
        questionItemService.removeById(itemId);
        return Result.msgSuccess("删除成功");
    }
    @Operation(summary = "添加/更新选项")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated QuestionItem questionItem){
        Question question = questionService.getById(questionItem.getQuestionId());
        if(question==null||question.getTeacherId()!=UserAuthUtil.getUserId()){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //修改时防止篡改id
        if(questionItem.getId()!=null){
            LambdaUpdateWrapper<QuestionItem> updateWrapper=new LambdaUpdateWrapper<>();
//            questionItem.setId(question.getId());
            //更新时：这里不能修改正确选项
            Map<SFunction<QuestionItem, ?>, Object> queryMap=new HashMap<>();
            queryMap.put(QuestionItem::getId,questionItem.getId());
            queryMap.put(QuestionItem::getQuestionId,questionItem.getQuestionId());
            updateWrapper.allEq(queryMap);
            questionItemService.update(questionItem,updateWrapper);
            return Result.msgSuccess("更新成功");

        }else{
            //题目选择个数限制
            QuestionTypeEnum type = question.getType();
            int itemMax= type.getItemMax();
            long count = questionItemService.count(new LambdaQueryWrapper<QuestionItem>().eq(QuestionItem::getQuestionId, question.getId()));

            if(count>=itemMax){
                return Result.msgError("添加失败，达到选项个数最大限制");
            }
            //填空题，该选项时就是答案
            QuestionTypeEnum enumByValue = IBaseEnum.getEnumByValue(question.getType(), QuestionTypeEnum.class);
            questionItemMapper.insert(questionItem);
            return Result.success("添加成功",questionItem.getId());
        }
    }
    @Operation(summary = "更新题目正确选项")
    @PostMapping("/correct/{itemId}")
    public Result correct(@PathVariable Integer itemId){
        QuestionItem item = questionItemService.getById(itemId);
        Question question = questionService.getById(item.getQuestionId());
        if(question==null||question.getTeacherId()!=UserAuthUtil.getUserId()){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        QuestionTypeEnum enumByValue = question.getType();
        QuestionItem questionItem = new QuestionItem();
        if(enumByValue==QuestionTypeEnum.SIGNAL_CHOICE||enumByValue==QuestionTypeEnum.JUDGMENTAL){
            //先把其他选项，去了
            questionItem.setAnswer(null);
            LambdaUpdateWrapper<QuestionItem> updateWrapper=new LambdaUpdateWrapper<>();
            updateWrapper.eq(QuestionItem::getQuestionId,question.getId());
            questionItemService.update(questionItem,updateWrapper);
            questionItem.setAnswer("1");

        }else if(enumByValue==QuestionTypeEnum.MULTIPLE_CHOICE){
            //多选题取反
            if(item.getAnswer()==null){
                questionItem.setAnswer("1");
                log.info("修改成正确答案"+questionItem.getAnswer());
            }else{
                //判断多选个数，如果就一个答案禁止取消
//                LambdaQueryWrapper<QuestionItem> queryWrapper=new LambdaQueryWrapper<>();
//                queryWrapper.eq(QuestionItem::getQuestionId,question.getId());
//                long count = questionItemService.count(queryWrapper);
//                if(count==1){
//                    return Result.msgError("多选答案不能为空");
//                }
                log.info("修改成错误答案");
                questionItem.setAnswer(null);
            }
        }
        questionItem.setId(itemId);
        questionItemService.updateById(questionItem);
        return Result.msgSuccess("更新成功");
        // 单选，判断，唯一
        // 多选，更改答案
    }
}
