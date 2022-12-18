package com.baymax.exam.center.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baymax.exam.center.model.ExamAnswerResult;
import com.baymax.exam.center.model.ExamInfo;
import com.baymax.exam.center.service.impl.ExamAnswerResultServiceImpl;
import com.baymax.exam.center.service.impl.ExamInfoServiceImpl;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 考试作答结果 前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-11-04
 */
@RestController
@RequestMapping("/exam-answer-result")
public class ExamAnswerResultController {

}
