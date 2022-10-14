package com.baymax.exam.user.controller;

import com.baymax.exam.common.core.base.ExamAuth;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.common.redis.utils.RedisUtil;
import com.baymax.exam.model.Classes;
import com.baymax.exam.model.Courses;
import com.baymax.exam.model.JoinClass;
import com.baymax.exam.user.service.impl.ClassesServiceImpl;
import com.baymax.exam.user.service.impl.CoursesServiceImpl;
import com.baymax.exam.user.service.impl.JoinClassServiceImpl;
import com.baymax.exam.user.utils.RandomUtil;
import com.baymax.exam.vo.ClassCodeVo;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-10-11
 */
@Tag(name = "班级管理")
@Slf4j
@Validated
@RestController
@RequestMapping("/classes")
public class ClassesController {
    /**
     * 课程服务
     */
    @Autowired
    CoursesServiceImpl coursesService;
    @Autowired
    ClassesServiceImpl classesService;
    @Autowired
    JoinClassServiceImpl joinClassService;

    @Operation(summary = "创建/修改班级")
    @PostMapping("/update")
    public Result  update(
            @RequestBody @Validated Classes classes){
        Courses course = coursesService.getById(classes.getCourseId());
        Integer userId = UserAuthUtil.getUserId();
        //得要是自己的课程，才能创建班级
        if(course==null||course.getUserId()!=userId){
            return  Result.failed(ResultCode.PARAM_ERROR);
        }
        String info="班级创建成功";
        //的要是自己的班级，才能更新
        if(course.getId()!=null){
            Classes clas = classesService.getById(course.getId());
            if (clas==null||clas.getTeacherId()!=userId){
                return  Result.failed(ResultCode.PARAM_ERROR);
            }
            //更新不能修改课程id
            classes.setCourseId(null);
            info="班级信息更新成功~";
        }
        classes.setTeacherId(UserAuthUtil.getUserId());
        classesService.saveOrUpdate(classes);
        return Result.success(info);
    }
    @Operation(summary = "删除班级")
    @PostMapping("/delete/{classId}")
    public Result delete(
            @Schema(description = "班级id")@PathVariable Integer classId){
        Classes classes = classesService.getById(classId);
        if(classes==null||classes.getTeacherId()!=UserAuthUtil.getUserId()){
            return  Result.failed(ResultCode.PARAM_ERROR);
        }
        classesService.removeById(classId);
        return Result.success("删除成功");
    }
    @Operation(summary = "获取班级列表")
    @GetMapping("/{courseId}/list")
    public Result<List<Classes>> getClassList(@PathVariable Integer courseId){
        Integer userId = UserAuthUtil.getUserId();
        Courses courses = coursesService.getById(courseId);
        List<Classes> list = null;
        //老师获取班级列表;
        if(courses==null||courses.getUserId()!=userId){
            list=classesService.getClassByCourseId(courseId);
        }else{
            JoinClass joinClass = joinClassService.getJoinByCourseId(userId, courseId);
            if(joinClass==null){
                return Result.msgError("未加入该课程");
            }
            list=new ArrayList<>();
            list.add( classesService.getById(joinClass.getClassId()));
        }
        return Result.success(list);
    }

    @Operation(summary = "获取班级码")
    @GetMapping("/classCode/{classId}")
    public Result getClassCode(
            @Schema(description = "班级号") @PathVariable Integer classId,
            @Schema(description = "true:重新生成") @RequestParam Boolean anew){
        //判断是不是我的课
        Classes myClass = classesService.getById(classId);
        if(myClass==null||myClass.getTeacherId()!=UserAuthUtil.getUserId()){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //班级码是否存在
        String code = null;
        LocalDateTime localDateTime=LocalDateTime.now();
        if(!anew){
            code=classesService.getCodeById(classId);
        }
        //生成班级码
        if(code==null){
            code=classesService.generateCode(classId);
        }
        //获取失效时间
        localDateTime.plusSeconds(classesService.getCodeValidTime(classId));
        ClassCodeVo classCode = new ClassCodeVo();
        classCode.setCode(code);
        classCode.setExpirationTime(localDateTime);
        return Result.success(classCode);
    }
    @Operation(summary = "加入班级")
    @PostMapping("/join/{code}")
    public Result joinClass(
            @Schema(description = "班级码") @PathVariable String code){
        Integer classId = classesService.getClassByCode(code);
        if(classId==null){
            return Result.msgInfo("班级码不存在");
        }
        Integer userId = UserAuthUtil.getUserId();
        JoinClass isJoinClass = joinClassService.getJoinByClassId(userId, classId);
        if(isJoinClass!=null){
            return Result.msgInfo("请勿重复加入班级");
        }
        JoinClass joinClass=new JoinClass();
        joinClass.setClassId(classId);
        joinClass.setStudentId(userId);
        joinClassService.save(joinClass);
        return Result.success("加入成功~");
    }
}
