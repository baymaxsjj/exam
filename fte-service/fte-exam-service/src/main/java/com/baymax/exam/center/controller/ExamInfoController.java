package com.baymax.exam.center.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baymax.exam.VueRouteLocationRaw;
import com.baymax.exam.center.model.*;
import com.baymax.exam.center.service.impl.*;
import com.baymax.exam.center.vo.ExamInfoVo;
import com.baymax.exam.common.core.exception.ResultException;
import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.message.enums.MessageTypeEnum;
import com.baymax.exam.message.feign.MessageServiceClient;
import com.baymax.exam.message.model.MessageInfo;
import com.baymax.exam.user.feign.ClassesClient;
import com.baymax.exam.user.feign.CourseClient;
import com.baymax.exam.user.feign.UserClient;
import com.baymax.exam.user.model.Classes;
import com.baymax.exam.user.model.Courses;
import com.baymax.exam.user.model.JoinClass;
import com.baymax.exam.web.utils.UserAuthUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
@Slf4j
@Tag(name = "考试管理")
@RestController
@RequestMapping("/exam-info")
public class ExamInfoController {
    @Autowired
    ExamInfoServiceImpl examInfoService;
    @Autowired
    ExamClassServiceImpl examClassService;
    @Autowired
    ExamPaperServiceImpl examService;
    @Autowired
    ExamQuestionServiceImpl examQuestionService;

    @Autowired
    ExamCenterServiceImpl examCenterService;
    @Autowired
    ExamPaperServiceImpl examPaperService;
    @Autowired
    CourseClient courseClient;
    @Autowired
    MessageServiceClient messageServiceClient;

    @Autowired
    ClassesClient classesClient;

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
            //考试考试后不准开始修改时间
            if(examInfo.getStartTime().isAfter(LocalDateTime.now())){
               examInfo.setStartTime(null);
               //删除考试信息缓存
            }
            info = "更新成功";
        } else {
            Courses course = courseClient.findCourse(examInfo.getCourseId());
            if (course != null) {
                teacherId = course.getUserId();
            }
            if(examInfo.getStartTime().isBefore(LocalDateTime.now())){
                return Result.msgInfo("考试时间不合法");
            }
        }
        Integer userId = UserAuthUtil.getUserId();
        if (teacherId != userId) {
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        // 如果设置了提交时间
        if(examInfo.getSubmitTime()!=null){
            if(examInfo.getSubmitTime().isAfter(examInfo.getEndTime())){
                return Result.msgWaring("提交时间不能超过结束时间");
            }
        }
        //试卷是不是我的
        ExamPaper examPaper = examService.getById(examInfo.getExamId());
        if(examPaper.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //TODO:班级是不是我的＞﹏＜
        //TODO:如果考试开始了，就不能更改试卷了
        //课程通知

        //删除试卷考试班级，然后在添加
        if (examInfo.getId()  != null) {
            LambdaQueryWrapper<ExamClass> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(ExamClass::getExamInfoId,examInfo.getId());
            examClassService.remove(queryWrapper);
        }
        examInfo.setTeacherId(userId);
        //更新试卷信息
        examInfoService.saveOrUpdate(examInfo);
        //删除缓存
        examCenterService.deleteCacheExamInfo(examInfo.getId());
        //重新添加考试班级
        List<ExamClass> list=examInfoVo.getClassIds().stream().map(cId ->{
            ExamClass examClass = new ExamClass();
            examClass.setClassId(cId);
            examClass.setExamInfoId(examInfo.getId());
            if(examInfo.getStartTime()!=null){
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setTitle(examInfo.getTitle());
                final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                messageInfo.setIntroduce(examInfo.getStartTime().format(dateTimeFormatter)+"~"+examInfo.getEndTime().format(dateTimeFormatter));
                messageInfo.setUserId(userId);
                messageInfo.setClientId(UserAuthUtil.getUser().getClientId());
                messageInfo.setTargetId(cId);
                VueRouteLocationRaw vueRouteLocationRaw = new VueRouteLocationRaw();
                vueRouteLocationRaw.setName("ExamManage");
                vueRouteLocationRaw.setParams(Map.of("",examInfo.getCourseId()));
                messageInfo.setPath(vueRouteLocationRaw.getJson());
                log.info("考试通知消息内容：{}", JSONUtil.toJsonStr(messageInfo));
                messageServiceClient.systemCourseMessage(messageInfo);
            }
            return examClass;
        }).collect(Collectors.toList());
        examClassService.saveBatch(list);
        return Result.msgSuccess(info);
    }
    @Operation(summary = "获取考试信息")
    @GetMapping("/detail/{examInfoId}")
    public Result<ExamInfoVo> detail(@PathVariable Integer examInfoId) throws ResultException {
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
        paper.setPaper(examPaperService.getById(examInfo.getExamId()));
        //班级信息
        Set<Integer> classIds = list.stream().map(item -> item.getClassId()).collect(Collectors.toSet());
        Result<List<Classes>> classListByIds = classesClient.getClassListByIds(classIds, examInfo.getCourseId());
        paper.setClassList(classListByIds.getResultDate());
        paper.setClassIds(classIds);
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
        Courses course = courseClient.findCourse(courseId);
        if(course==null){
            return Result.failed(ResultCode.PARAM_ERROR);
        }

        QueryWrapper<ExamInfo> queryWrapper=new QueryWrapper();
        switch (status){
            case 1:
                //未开始
                queryWrapper.apply(true,
                                "date_format (start_time,'%Y-%m-%d %H:%i:%S') > date_format(now(),'%Y-%m-%d %H:%i:%S')");

                break;
            case 2:
                //进行中
                queryWrapper.apply(true,
                        "date_format (start_time,'%Y-%m-%d %H:%i:%S')<= date_format(now(),'%Y-%m-%d %H:%i:%S')")
                .apply(true,
                        "date_format (end_time,'%Y-%m-%d %H:%i:%S') >= date_format(now(),'%Y-%m-%d %H:%i:%S')");
                break;
            case 3:
                //结束
                queryWrapper.apply(true,
                        "date_format (end_time,'%Y-%m-%d %H:%i:%S') < date_format(now(),'%Y-%m-%d %H:%i:%S')");
                break;
        }
        queryWrapper.orderByDesc("created_at");
        Page<ExamInfo> pa =new Page(page,pageSize);
        IPage<ExamInfo> record;
        if(course.getUserId()==userId){
            Map<String, Object> queryMap=new HashMap<>();
            queryMap.put("course_id",courseId);
            queryMap.put("teacher_id",userId);
            queryWrapper.allEq(queryMap);
            record= examInfoService.page(pa, queryWrapper);
        }else{
            JoinClass joinClass = courseClient.joinCourseByStuId(courseId, userId);
            if(joinClass==null){
                return Result.failed(ResultCode.PARAM_ERROR);
            }
            queryWrapper.eq("class_id",joinClass.getClassId());
            record=examInfoService.getSutExamInfo(pa,queryWrapper);
        }
        return Result.success(PageResult.setResult(record));
    }
}
