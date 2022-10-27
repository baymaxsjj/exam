package com.baymax.exam.center.controller;

import com.baymax.exam.center.model.Exam;
import com.baymax.exam.center.model.ExamClass;
import com.baymax.exam.center.service.impl.ExamClassServiceImpl;
import com.baymax.exam.center.service.impl.ExamServiceImpl;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.user.feign.UserServiceClient;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 考试班级 前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-10-26
 */
@Validated
@Tag(name = "班级考试管理")
@RestController
@RequestMapping("/exam-class")
public class ExamClassController {
    @Autowired
    ExamServiceImpl examService;
    @Autowired
    ExamClassServiceImpl examClassService;
    @Autowired
    UserServiceClient userServiceClient;
    @Operation(summary = "班级添加考试信息")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated ExamClass examClass){
        Exam exam = examService.getById(examClass.getExamId());
        Integer userId = UserAuthUtil.getUserId();
        if(exam==null||exam.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //TODO:判断班级是不是我的
        examClassService.saveOrUpdate(examClass);
        return Result.success("添加成功");
    }
}
