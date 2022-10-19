package com.baymax.exam.user.controller;

import com.baymax.exam.user.model.Classes;
import com.baymax.exam.user.model.Courses;
import com.baymax.exam.user.model.JoinClass;
import com.baymax.exam.user.model.User;
import com.baymax.exam.user.service.impl.ClassesServiceImpl;
import com.baymax.exam.user.service.impl.CoursesServiceImpl;
import com.baymax.exam.user.service.impl.JoinClassServiceImpl;
import com.baymax.exam.user.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/9 19:41
 * @description：远程调用接口
 * @modified By：
 * @version:
 */
@Tag(name = "用户相关远程接口")
@RestController
@RequestMapping("/inner")
public class RemoteController
{
    @Autowired
    UserServiceImpl userService;
    @Autowired
    CoursesServiceImpl coursesService;
    @Autowired
    JoinClassServiceImpl joinClassService;

    @Operation(summary = "用户名/邮箱获取用户信息")
    @GetMapping("/findUser")
    public User findUser(String username){
        User user = null;
        if(username.contains("@")){
            user=userService.getUserByEmail(username);
        }else{
            user=userService.getUserByUserName(username);
        }
        return user;
    }

    @Operation(summary = "id获取课程信息")
    @GetMapping("/findCourse")
    public Courses findCourse(Integer courseId){
        return coursesService.getById(courseId);
    }
    @Operation(summary = "获取课程参加信息")
    @GetMapping("/joinCourseByStuId")
    public JoinClass joinCourseByStuId(Integer courseId, Integer stuId){
        return joinClassService.getJoinByCourseId(stuId,courseId);
    }
}
