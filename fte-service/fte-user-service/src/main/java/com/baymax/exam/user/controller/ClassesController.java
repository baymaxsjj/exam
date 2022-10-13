package com.baymax.exam.user.controller;

import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.model.Classes;
import com.baymax.exam.model.Courses;
import com.baymax.exam.user.service.impl.CoursesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-10-11
 */
@Validated
@RestController
@RequestMapping("/classes")
public class ClassesController {
    @Autowired
    CoursesServiceImpl coursesService;
    public Result  add(@RequestBody @Validated Classes classes){
        Courses course = coursesService.getById(classes.getCourseId());
        return Result.success("班级创建成功");
    }
}
