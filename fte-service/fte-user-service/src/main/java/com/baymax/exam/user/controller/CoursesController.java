package com.baymax.exam.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.model.Courses;
import com.baymax.exam.user.service.impl.CoursesServiceImpl;
import com.baymax.exam.vo.CourseInfoVo;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.websocket.server.PathParam;

/**
 * <p>
 * 课程信息 前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-10-11
 */
@Validated
@Tag(name = "课程管理")
@RestController
@RequestMapping("/courses")
public class CoursesController {
    @Autowired
    CoursesServiceImpl coursesService;

    @Operation(summary = "创建课程")
    @PostMapping("/update")
    public Result add(@RequestBody @Validated Courses courses) {
        //更新判断
        Integer courseId = courses.getId();
        Integer userId = UserAuthUtil.getUserId();
        //更新判断：是不是自己的课程
        if(courseId!=null){
            Courses cour = coursesService.getById(courseId);
            if(cour==null||cour.getUserId()!=userId){
                return Result.failed(ResultCode.PARAM_ERROR);
            }
        }
        courses.setUserId(userId);
        coursesService.saveOrUpdate(courses);
        return Result.success("创建成功");
    }

    @Operation(summary = "获取课程课程")
    @GetMapping("/get")
    public Result<Courses> getCourse(@RequestParam Integer courseId) {
        return Result.success(coursesService.getCourse(courseId, UserAuthUtil.getUserId()));
    }

    @Operation(summary = "分页获取课程列表")
    @GetMapping("/list/{role}")
    public Result<PageResult<CourseInfoVo>> getCourseList(
            @PathVariable @Schema(description = "student/teacher") String role,
            @Schema(description = "0:开课：1：结课") @RequestParam  String status,
            @RequestParam Long currentPage) {
        boolean isStudent = true;
        int stu=0;
        if ("teacher".equals(role)) {
            isStudent = false;
        }
        if("1".equals(status)){
            stu=1;
        }
        IPage<CourseInfoVo> courseList = coursesService.getCourseList(UserAuthUtil.getUserId(),stu, isStudent, currentPage, 10 );
        return Result.success(PageResult.setResult(courseList));
    }

    @Operation(summary = "修改公开/结课状态")
    @PostMapping("/update/{status}")
    public Result updatePublicOrStatus(
            @Schema(description = "status:：status/public") @PathVariable String status,
            @Schema(description = "课程id")@RequestParam Integer courseId,
            @Schema(description = "状态:0：开课/隐藏，1：结课/公共") @RequestParam String isPublic) {
        Courses myCourse = coursesService.getById(courseId);
        if(myCourse==null||myCourse.getUserId()!=UserAuthUtil.getUserId()){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        Courses courses = new Courses();
        courses.setUserId(UserAuthUtil.getUserId());
        courses.setId(courseId);
        if ("public".equals(status)) {
            courses.setStatus(isPublic);
        } else {
            courses.setIsPublic(isPublic);
        }
        coursesService.updateById(courses);
        return Result.success("修改成功");
    }
}
