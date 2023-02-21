package com.baymax.exam.center.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baymax.exam.center.enums.*;
import com.baymax.exam.center.excel.StudentReviewExcel;
import com.baymax.exam.center.model.*;
import com.baymax.exam.center.service.impl.*;
import com.baymax.exam.center.vo.AnswerQuestionResultVo;
import com.baymax.exam.center.vo.QuestionInfoVo;
import com.baymax.exam.center.vo.StudentActionVo;
import com.baymax.exam.center.vo.StudentReviewVo;
import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.user.feign.CourseClient;
import com.baymax.exam.user.feign.JoinClassClient;
import com.baymax.exam.user.feign.UserAuthInfoClient;
import com.baymax.exam.user.model.UserAuthInfo;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.rmi.server.ExportException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/18 9:40
 * @description：考试控制台
 * @modified By：
 * @version:
 */
@Slf4j
@Validated
@Tag(name = "考试控制台")
@RestController
@RequestMapping("/exam-console/{examInfoId}")
public class ExamConsoleController {
    @Autowired
    CourseClient courseClient;
    @Autowired
    JoinClassClient joinClassClient;
    @Autowired
    UserAuthInfoClient userAuthInfoClient;
    @Autowired
    ExamAnswerLogServiceImpl examAnswerLogService;
    @Autowired
    ExamInfoServiceImpl examInfoService;
    @Autowired
    QuestionServiceImpl questionService;
    @Autowired
    ExamCenterServiceImpl examCenterService;
    @Autowired
    ExamScoreRecordServiceImpl scoreRecordService;
    @Autowired
    ExamAnswerResultServiceImpl answerResultService;

    @Autowired
    ExamConsoleServiceImpl examConsoleService;
    @Autowired
    ExamClassServiceImpl examClassService;
    /**
     * 获取总览
     * 答题进度、
     *
     * @param examInfoId 考试信息id
     * @return {@link Result}
     */
    @Operation(summary = "获取概况/监控")
    @PostMapping("/outline-monitor")
    public Result getOutline(
            @PathVariable Integer examInfoId,
            @RequestParam(required = false) Integer classId,
            @RequestParam AnswerStatusEnum status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        Integer userId = UserAuthUtil.getUserId();
        if(examInfo==null|| !Objects.equals(examInfo.getTeacherId(), userId)){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //TODO:判断班级是不是自己的
        Set<Integer> studentIds=null;
        boolean isIsList=true;
        // 查询行为日志
        ExamAnswerLogEnum answerLogEnum=null;
        switch (status){
            case NOT_START:
                isIsList=false;
            case START:
                answerLogEnum=ExamAnswerLogEnum.START;
                break;
            case SUBMIT:
                answerLogEnum=ExamAnswerLogEnum.SUBMIT;
                break;
        }
        List<ExamAnswerLog> distinctLogList = examAnswerLogService.getDistinctLogList(examInfoId, answerLogEnum,page,pageSize).getRecords();
        //查全部
        if(status!=AnswerStatusEnum.ALL){
            studentIds=distinctLogList.stream().map(ExamAnswerLog::getStudentId).collect(Collectors.toSet());
        }
        //获取学生信息列表
        PageResult<UserAuthInfo> batchClassUser =examConsoleService.getUserInfo(examInfoId,examInfo.getCourseId(),null,null,isIsList,page,pageSize);
        //整合行为列表
        List<UserAuthInfo> studentList = batchClassUser.getList();
        List<StudentActionVo> studentsActionInfo = studentList.stream().map(studentInfo -> {
            StudentActionVo studentActionVo = new StudentActionVo();
            studentActionVo.setUserAuthInfo(studentInfo);
            //如果存在日志,就返回后10条
            IPage<ExamAnswerLog> userLog = examAnswerLogService.getLogListByUserId(examInfoId,studentInfo.getUserId(),1,10);
            if(!userLog.getRecords().isEmpty()){
                Collections.reverse(userLog.getRecords());
                 studentActionVo.setActionPage(PageResult.setResult(userLog));
                 userLog.getRecords().stream().filter(log->log.getStatus()==ExamAnswerLogEnum.SUBMIT).findFirst().ifPresentOrElse(log->{
                     studentActionVo.setAnswerStatus(AnswerStatusEnum.SUBMIT);
                    },()->{
                     studentActionVo.setAnswerStatus(AnswerStatusEnum.START);
                    }
                 );
            }else{
                studentActionVo.setAnswerStatus(AnswerStatusEnum.NOT_START);
            }
            return studentActionVo;
        }).collect(Collectors.toList());
        return Result.success(PageResult.copyPageResult(batchClassUser,studentsActionInfo));
    }
    @Operation(summary = "获取批阅列表")
    @PostMapping("/review/list")
    public Result getReViewList(
            @RequestParam(required = false,defaultValue = "all") String reviewType,
            @RequestParam(required = false) Integer classId,
            @PathVariable Integer examInfoId,
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(required = false,defaultValue = "10") Long pageSize){
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        Integer userId = UserAuthUtil.getUserId();
        if(examInfo==null||examInfo.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        return Result.success(examConsoleService.getReViewList(reviewType,classId,examInfoId,page,pageSize));
    }
    @Operation(summary = "导出批阅列表")
    @PostMapping("/review/list/export")
    public void reviewListExport(
            HttpServletResponse res,
            @RequestParam(required = false,defaultValue = "all") String reviewType,
            @RequestParam(required = false) Integer classId,
            @PathVariable Integer examInfoId) throws IOException {
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        Integer userId = UserAuthUtil.getUserId();
        if(examInfo==null||examInfo.getTeacherId()!=userId){
            throw new ExportException("");
        }
        //获取xx打印 表格
        //定义输出文件名称
        String fileName = URLEncoder.encode(examInfo.getTitle()+"-考试成绩" + ".xlsx","UTF-8") ;
        //设置响应字符集
        res.setCharacterEncoding("UTF-8");
        //设置响应媒体类型
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        //设置响应的格式说明
//        res.setHeader("Content-Disposition", "attachment;filename="+fileName);
        res.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
        //读取响应文件的模板
        File file= ResourceUtils.getFile("classpath:template/考试成绩模板.xlsx");
        //替换模板的数据
        // 方案3 分多次 填充 会使用文件缓存（省内存）
        try (ExcelWriter excelWriter = EasyExcel.write(res.getOutputStream()).withTemplate(file).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            PageResult<StudentReviewVo> reviewList;
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            AtomicInteger joinNumber= new AtomicInteger();
            long page=1;
            do{
                reviewList= examConsoleService.getReViewList(reviewType, classId, examInfoId, page, 100L);
                if(reviewList!=null){
                     List<StudentReviewExcel> reviewExcels = reviewList.getList().stream().map(studentReviewVo -> {
                         if(studentReviewVo.getAnswerStatus()!=null){
                             joinNumber.getAndIncrement();
                         }
                         return  new StudentReviewExcel(studentReviewVo);
                     }).collect(Collectors.toList());
                     log.info("Excels list:{}",reviewExcels);
                    excelWriter.fill(reviewExcels,fillConfig, writeSheet);
                    page++;
                }
            }while (reviewList!=null&&page<=reviewList.getPages());
            Map<String, Object> map = MapUtils.newHashMap();
            map.put("join", joinNumber.get());
            map.put("total", reviewList.getTotal());
            excelWriter.fill(map,fillConfig, writeSheet);
            excelWriter.finish();
        }
//        return Result.failed(ResultCode.PARAM_ERROR);
    }

    @Operation(summary = "获取批阅列表")
    @GetMapping("/review/{studentId}")
    public Result getReViewAnswer(
            @PathVariable Integer examInfoId,
            @PathVariable Integer studentId) {
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        Integer userId = UserAuthUtil.getUserId();
        //老师查看
        if (examInfo == null || !Objects.equals(examInfo.getTeacherId(), userId)) {
            //自己查看自己
            if(!(Objects.equals(studentId, userId) &&examInfo.getEndVisible())){
                return Result.failed(ResultCode.PARAM_ERROR);
            }
            if(!LocalDateTime.now().isAfter(examInfo.getEndTime())){
                return Result.msgInfo("考试期间禁止查看");
            }
        }
        //获取考试题目
        List<QuestionInfoVo> questionInfoVos = questionService.examQuestionInfo(examInfo.getExamId());
        log.info("学生作答/题目信息：{}",questionInfoVos);
        //获取作答
        final List<ExamAnswerResult> answerResultList = answerResultService.getAnswerResultListByUserId(examInfoId, studentId);
       if(answerResultList.isEmpty()){
           return Result.msgWaring("未获取到作答信息");
       }
        //获取批阅
        final List<ExamScoreRecord> scoreList = scoreRecordService.getScoreListByUserId(examInfoId, studentId);
        //整合
        List<AnswerQuestionResultVo>  answerQuestionResultList = questionInfoVos.stream().map(questionInfo -> {
            AnswerQuestionResultVo answerQuestionResult = new AnswerQuestionResultVo();
            List<ExamAnswerResult> answerResults = answerResultList.stream().filter(answerResult -> Objects.equals(answerResult.getQuestionId(), questionInfo.getId())).toList();
            answerQuestionResult.setAnswerResult(answerResults);
            scoreList.stream().filter(score -> Objects.equals(score.getQuestionId(), questionInfo.getId())).findFirst().ifPresent(score -> {
                answerQuestionResult.setScoreRecord(score);
            });
            answerQuestionResult.setQuestionInfo(questionInfo);
            return answerQuestionResult;
        }).toList();
        log.info("学生作答/批阅信息：{}",answerQuestionResultList);
        //2.题目类型分类
        //题目类型排序
        Map<QuestionTypeEnum, List<AnswerQuestionResultVo>> resultGroup = answerQuestionResultList.stream().collect(Collectors.groupingBy(result -> result.getQuestionInfo().getType()));
        //题目类型排序
        TreeMap<QuestionTypeEnum, List<AnswerQuestionResultVo>> questionResultOrderGroup=new TreeMap<>(Comparator.comparing(QuestionTypeEnum::getValue));
        questionResultOrderGroup.putAll(resultGroup);
        log.info("学生作答/批阅信息：分组信息：有序{}",questionResultOrderGroup);
        StudentReviewVo studentReview=new StudentReviewVo();
        examCenterService.statisticalAnswerInfo(studentReview,scoreList,questionInfoVos);
        //防止非法获取认证信息
        UserAuthInfo userAuthInfo = userAuthInfoClient.getStudentInfo(studentId);
        studentReview.setUserAuthInfo(userAuthInfo);
        studentReview.setAnswerResults(questionResultOrderGroup);

        return Result.success(studentReview);
    }
    @Operation(summary = "获取考试人数")
    @GetMapping("/student-number")
    public Result<Long> getReViewAnswer(@PathVariable Integer examInfoId){
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        if(examInfo==null||examInfo.getTeacherId()!=UserAuthUtil.getUserId()){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        List<ExamClass> examClassIds = examClassService.getExamClassIds(examInfoId);
        return joinClassClient.getStudentNumberByIds(examClassIds.stream().map(examClass -> examClass.getClassId()).collect(Collectors.toSet()));
    }
    /*
    * 1.题型正确率
    * 2.题目正确率
    * 3.平均分
    * 3.最高分，最低分
    * 3.方差
    *
    * */
    @GetMapping ("/exam/result-statistical")
    Result getExamResultStatistical(@RequestParam(required = false) Integer classId, @PathVariable Integer examInfoId){
        //获取用户列表
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        Integer userId = UserAuthUtil.getUserId();
        if(examInfo==null|| !Objects.equals(examInfo.getTeacherId(), userId)){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        List<ExamScoreRecord> scoreList;
        if(classId!=null){
            scoreList=scoreRecordService.getScoreListByClassId(examInfoId,classId);
        }else{
            scoreList= scoreRecordService.getScoreListByExamId(examInfoId);
        }
        if(scoreList.isEmpty()){
            return Result.success();
        }
        //获取考试题目
        List<Question> questionInfoVos = questionService.examQuestion(examInfo.getExamId());
        Map<String,ExamResultStatistic.AnswerStatisticInfo> questionStatistics=new HashMap<>();
        Map<QuestionTypeEnum,ExamResultStatistic.AnswerStatisticInfo> typeStatistics=new HashMap<>();
        //统计题目正确率
        scoreList.stream().collect(Collectors.groupingBy(ExamScoreRecord::getQuestionId)).forEach((k,v)->{
            //获取题目
             questionInfoVos.stream().filter(q -> q.getId().equals(k)).findFirst().ifPresent(question->{
                 log.info("题目：{}",k);
                 v.forEach(scoreRecord -> {
                     Float score = scoreRecord.getScore();
                     log.info("分数：{},成绩：{},相等：{}",question.getScore(),score,question.getScore()==score);
                     String content=question.getContent();
                     QuestionTypeEnum type=question.getType();
                     questionStatistics.computeIfAbsent(content,key->new ExamResultStatistic.AnswerStatisticInfo()).autoAddTotalNumber();
                     typeStatistics.computeIfAbsent(type,key->new ExamResultStatistic.AnswerStatisticInfo()).autoAddTotalNumber();
                     if(Math.abs(question.getScore()-score)<=0){
                         questionStatistics.get(content).autoAddCorrectNumber();
                         typeStatistics.get(type).autoAddCorrectNumber();
                     }
                 });
             });
            //统计正确率
        });
       //统计平均分
        Double totalScore=scoreList.stream().mapToDouble(ExamScoreRecord::getScore).sum();
        Long number=scoreList.stream().mapToInt(ExamScoreRecord::getUserId).distinct().count();
        //统计
        AtomicReference<Integer> maxScoreUserId = new AtomicReference<>();
        AtomicReference<Integer> minScoreUserId = new AtomicReference<>();
        AtomicReference<Float> maxScore= new AtomicReference<>(0F);
        AtomicReference<Float> minScore= new AtomicReference<>(0F);
        final Map<Integer, Float> scoreCollect = scoreList.stream().collect(Collectors.toMap(ExamScoreRecord::getUserId, ExamScoreRecord::getScore, Float::sum));
        for(Integer id :scoreCollect.keySet()){
            float score=scoreCollect.get(id);
            if(score> maxScore.get()){
                maxScore.set(score);
                maxScoreUserId.set(id);
            }
            if(score< minScore.get()||minScore.get()==0F){
                minScore.set(score);
                minScoreUserId.set(id);
            }
        }
        ExamResultStatistic.MostScoreInfo maxScoreInfo=new ExamResultStatistic.MostScoreInfo();
        ExamResultStatistic.MostScoreInfo minScoreInfo=new ExamResultStatistic.MostScoreInfo();
        if(minScoreUserId.get()!=null){
            maxScoreInfo.setUser(userAuthInfoClient.getStudentInfo(minScoreUserId.get()));
            maxScoreInfo.setScore(maxScore.get());

            minScoreInfo.setUser(userAuthInfoClient.getStudentInfo(maxScoreUserId.get()));
            minScoreInfo.setScore(minScore.get());
        }
        ExamResultStatistic statistics=new ExamResultStatistic();
        statistics.setAverageScore(Math.round(totalScore/number));
        statistics.setMaxScoreInfo(maxScoreInfo);
        statistics.setMinScoreInfo(minScoreInfo);
        statistics.setQuestionStatistic(questionStatistics);
        statistics.setQuestionTypeStatistic(typeStatistics);
        return Result.success(statistics);
    }
}
