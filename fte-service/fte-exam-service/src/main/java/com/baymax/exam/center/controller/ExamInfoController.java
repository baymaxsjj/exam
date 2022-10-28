package com.baymax.exam.center.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baymax.exam.center.model.Exam;
import com.baymax.exam.center.model.ExamClass;
import com.baymax.exam.center.model.ExamInfo;
import com.baymax.exam.center.model.ExamQuestion;
import com.baymax.exam.center.service.impl.ExamClassServiceImpl;
import com.baymax.exam.center.service.impl.ExamInfoServiceImpl;
import com.baymax.exam.center.service.impl.ExamServiceImpl;
import com.baymax.exam.center.vo.ExamInfoVo;
import com.baymax.exam.center.vo.ExamPaperVo;
import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.user.feign.UserServiceClient;
import com.baymax.exam.user.model.Courses;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 考试信息 前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-10-28
 */
@Validated
@Tag(name = "考试管理")
@RestController
@RequestMapping("/exam-info")
public class ExamInfoController {
    @Autowired
    ExamInfoServiceImpl examInfoService;
    @Autowired
    ExamClassServiceImpl examClassService;
    @Autowired
    ExamServiceImpl examService;

    @Autowired
    UserServiceClient userServiceClient;

    @Operation(summary = "发布考试信息")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated ExamInfoVo examInfoVo){
        ExamInfo examInfo=examInfoVo.getExamInfo();
        String info = "添加成功";
        Integer teacherId = null;
        //更改，以前那种可能会查询两次数据库
        if (examInfo.getId() != null) {
            ExamInfo temExam = examInfoService.getById(examInfo.getId());
            if (temExam != null) {
                teacherId = temExam.getTeacherId();
            }
            info = "更新成功";
        } else {
            Courses course = userServiceClient.findCourse(examInfo.getCourseId());
            if (course != null) {
                teacherId = course.getUserId();
            }
        }
        Integer userId = UserAuthUtil.getUserId();
        if (teacherId != userId) {
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //试卷是不是我的
        Exam exam = examService.getById(examInfo.getExamId());
        if(exam.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //TODO:班级是不是我的＞﹏＜

        //删除试卷考试班级，然后在添加
        if (examInfo.getId()  != null) {
            LambdaQueryWrapper<ExamClass> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(ExamClass::getExamInfoId,examInfo.getId());
            examClassService.remove(queryWrapper);
        }
        examInfo.setTeacherId(userId);
        //更新试卷信息
        examInfoService.saveOrUpdate(examInfo);
        //重新添加考试班级
        List<ExamClass> list=examInfoVo.getClassList().stream().map(integer ->{
            ExamClass examClass = new ExamClass();
            examClass.setClassId(integer);
            examClass.setExamInfoId(examInfo.getId());
            return examClass;
        }).collect(Collectors.toList());
        examClassService.saveBatch(list);
        return Result.msgSuccess(info);
    }
    @Operation(summary = "获取考试信息")
    @GetMapping("/detail/{examInfoid}")
    public Result detail(@PathVariable Integer examInfoId){
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        Integer userId = UserAuthUtil.getUserId();
        if(examInfo==null||examInfo.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        LambdaQueryWrapper<ExamClass> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ExamClass::getExamInfoId,examInfoId);
        List<ExamClass> list = examClassService.list(queryWrapper);
        ExamInfoVo paper=new ExamInfoVo();
        paper.setExamInfo(examInfo);

        paper.setClassList(list.stream().map(item->item.getClassId()).collect(Collectors.toSet()));
        return Result.success(paper);
    }

    @Operation(summary = "删除考试")
    @PostMapping("/delete/{examInfoId}")
    public Result delete(@PathVariable Integer examInfoId){
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        Integer userId = UserAuthUtil.getUserId();
        if(examInfo==null||examInfo.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        examInfoService.removeById(examInfoId);
        return Result.msgSuccess("删除成功");
    }

    @Operation(summary = "获取考试列表")
    @GetMapping("/list/{courseId}")
    public Result list(@PathVariable Integer courseId,
                       @Schema(description = "0:全部、1：未开始、2：进行中、3：结束")
                       @RequestParam(defaultValue = "0",required = false) Integer status,
                       @RequestParam(defaultValue = "1",required = false) Integer page,
                       @RequestParam(defaultValue = "10",required = false) Integer pageSize
                       ){
        Integer userId = UserAuthUtil.getUserId();
        LambdaQueryWrapper<ExamInfo> queryWrapper=new LambdaQueryWrapper();
        Map<SFunction<ExamInfo, ?>, Object> queryMap=new HashMap<>();
        queryMap.put(ExamInfo::getCourseId,courseId);
        queryMap.put(ExamInfo::getTeacherId,userId);
        switch (status){
            case 1:
                //未开始
                queryWrapper.lt(ExamInfo::getStartTime, LocalDateTime.now());
                break;
            case 2:
                //进行中
                queryWrapper.gt(ExamInfo::getStartTime, LocalDateTime.now()).and(w->w.lt(ExamInfo::getEndTime, LocalDateTime.now()));
                break;
            case 3:
                //结束
                queryWrapper.gt(ExamInfo::getEndTime, LocalDateTime.now());
                break;
        }
        queryWrapper.allEq(queryMap);
        queryWrapper.orderByDesc(ExamInfo::getCreatedAt);
        Page<ExamInfo> pa=new Page(page,pageSize);
        Page<ExamInfo> record = examInfoService.page(pa, queryWrapper);
        return Result.success(PageResult.setResult(record));
    }
}
