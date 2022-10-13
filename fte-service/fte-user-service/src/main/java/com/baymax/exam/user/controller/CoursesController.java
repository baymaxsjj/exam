package com.baymax.exam.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.common.core.result.Result;
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
    @PostMapping("/add")
    public Result add(@RequestBody @Validated Courses courses) {
        courses.setUserId(UserAuthUtil.getUserId());
        coursesService.save(courses);
        return Result.success("创建成功");
    }

    @Operation(summary = "获取课程课程")
    @GetMapping("/get")
    public Result<Courses> getCourse(@RequestParam Integer courseId) {
        return Result.success(coursesService.getCourse(courseId, UserAuthUtil.getUserId()));
    }

    @Operation(summary = "分页获取课程列表")
    @GetMapping("/list")
    public PageResult<CourseInfoVo> getCourseList(
            @RequestParam @Schema(description = "student/teacher") String role,
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
        return PageResult.success(courseList);
    }

    @Operation(summary = "修改课程")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated Courses courses) {
        courses.setUserId(UserAuthUtil.getUserId());
        boolean boo = coursesService.updateCourse(courses);
        if (boo) {
            return Result.success("更新成功");
        } else {
            return Result.msgWaring("更新失败");
        }
    }

    @Operation(summary = "修改公开/结课状态")
    @PostMapping("/update/{status}")
    public Result updatePublic(
            @Schema(description = "status:：status/public") @PathVariable String status,
            @Schema(description = "课程id")@RequestParam Integer courseId,
            @Schema(description = "状态:0：开课/隐藏，1：结课/公共") @RequestParam String isPublic) {
        Courses courses = new Courses();
        courses.setUserId(UserAuthUtil.getUserId());
        courses.setId(courseId);
        if ("public".equals(status)) {
            courses.setStatus(isPublic);
        } else {
            courses.setIsPublic(isPublic);
        }
        return Result.success("修改成功");
    }
}
