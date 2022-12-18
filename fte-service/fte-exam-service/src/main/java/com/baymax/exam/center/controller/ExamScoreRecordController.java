package com.baymax.exam.center.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baymax.exam.center.enums.ReviewTypeEnum;
import com.baymax.exam.center.mapper.ExamScoreRecordMapper;
import com.baymax.exam.center.model.ExamAnswerResult;
import com.baymax.exam.center.model.ExamInfo;
import com.baymax.exam.center.model.ExamScoreRecord;
import com.baymax.exam.center.service.impl.ExamAnswerResultServiceImpl;
import com.baymax.exam.center.service.impl.ExamInfoServiceImpl;
import com.baymax.exam.center.service.impl.ExamScoreRecordServiceImpl;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 考试得分 前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-12-11
 */
@RestController
@RequestMapping("/exam-score-record")
public class ExamScoreRecordController {
    @Autowired
    ExamInfoServiceImpl examInfoService;
    @Autowired
    ExamScoreRecordMapper scoreRecordMapper;
    @Operation(summary = "更新题目分数")
    @PostMapping("/{examInfoId}/update/score")
    public Result reviewScoreUpdate(
            @RequestBody @Validated List<ExamScoreRecord> scoreRecordList,
            @PathVariable Integer examInfoId){
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        Integer userId = UserAuthUtil.getUserId();
        if(examInfo==null|| !Objects.equals(examInfo.getTeacherId(), userId)){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //TODO:得分判断是否合规,算了吧，如果修改分数，那也是老师愿意
        scoreRecordList.forEach(examScoreRecord -> {
            examScoreRecord.setExamInfoId(examInfoId);
            examScoreRecord.setReviewType(ReviewTypeEnum.TEACHER);
        });
        scoreRecordMapper.batchUpdateByList(scoreRecordList);
        return Result.msgSuccess("批阅成功");
    }
}
