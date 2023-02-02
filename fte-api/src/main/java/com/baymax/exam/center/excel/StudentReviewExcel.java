package com.baymax.exam.center.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baymax.exam.center.enums.ExamAnswerLogEnum;
import com.baymax.exam.center.vo.StudentReviewVo;
import com.baymax.exam.user.model.Student;
import com.baymax.exam.user.model.UserAuthInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author ：Baymax
 * @date ：Created in 2023/1/2 7:30
 * @description：
 * @modified By：
 * @version:
 */
@Data
public class StudentReviewExcel extends StudentReviewVo {
    private Integer userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "别名")
    private String nickname;

    @Schema(description = "头像")
    private String picture;

    private Integer studentId;

    @Schema(description = "工号/学号")
    private String jobNo;

    @Schema(description = "姓名")
    private String realName;

    private String email;

    private String phone;

    private Integer departmentId;

    @Schema(description = "logo")
    private String departmentLogo;

    @Schema(description = "班级名称/部门")
    private String departmentName;

    @Schema(description = "上级部门")
    private Integer parentId;

    @Schema(description = "辅导员/部门管理")
    private Integer leaderId;

    private Integer schoolId;

    private String schoolLogo;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "学校域名")
    private String schoolSite;

    private String answerStatusAction;
    private Long time;
    public StudentReviewExcel(StudentReviewVo studentReview){
        UserAuthInfo userAuthInfo = studentReview.getUserAuthInfo();
        this.username=userAuthInfo.getUsername();
        this.nickname=userAuthInfo.getNickname();
        this.schoolName=userAuthInfo.getSchoolName();
        this.jobNo=userAuthInfo.getJobNo();
        this.departmentName=userAuthInfo.getDepartmentName();
        this.realName= userAuthInfo.getRealName();
        this.setReviewCount(studentReview.getReviewCount());
        this.setReviewTotal(studentReview.getReviewTotal());
        this.setScore(studentReview.getScore());
        if(studentReview.getAnswerStatus()==null){
            this.answerStatusAction="未参加";
        }else{
            this.answerStatusAction=studentReview.getAnswerStatus().getAction();
        }
        this.setCourseClassName(studentReview.getCourseClassName());
        this.setStartTime(studentReview.getStartTime());
        this.setSubmitTime(studentReview.getSubmitTime());
        this.setCorrectNumber(studentReview.getCorrectNumber());
        if(studentReview.getSubmitTime()!=null&&studentReview.getStartTime()!=null){
            Duration duration = Duration.between(studentReview.getStartTime(),studentReview.getSubmitTime());
            this.time=duration.toMinutes();
        }

    }
    public StudentReviewExcel(){}
}
