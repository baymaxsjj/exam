package com.baymax.exam.user.controller;

import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.user.model.Classes;
import com.baymax.exam.user.model.Courses;
import com.baymax.exam.user.model.JoinClass;
import com.baymax.exam.user.service.impl.ClassesServiceImpl;
import com.baymax.exam.user.service.impl.CoursesServiceImpl;
import com.baymax.exam.user.service.impl.JoinClassServiceImpl;
import com.baymax.exam.user.vo.ClassCodeVo;
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
        if(classes.getId()!=null){
            Classes clas = classesService.getById(classes.getId());
            if (clas==null||clas.getTeacherId()!=userId){
                return  Result.failed(ResultCode.PARAM_ERROR);
            }
            //更新不能修改课程id
            classes.setCourseId(null);
            info="班级信息更新成功~";
        }
        classes.setTeacherId(UserAuthUtil.getUserId());
        classesService.saveOrUpdate(classes);
        return Result.msgSuccess(info);
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
        return Result.msgSuccess("删除成功");
    }
    @Operation(summary = "获取班级信息")
    @GetMapping("/info/{classId}")
    public Result info(
            @Schema(description = "班级id")@PathVariable Integer classId){
        Classes classes = classesService.getById(classId);
        if(classes==null||classes.getTeacherId()!=UserAuthUtil.getUserId()){
            return  Result.failed(ResultCode.PARAM_ERROR);
        }
        return Result.success(classes);
    }
    @Operation(summary = "获取班级列表")
    @GetMapping("/{courseId}/list")
    public Result<List<Classes>> getClassList(@PathVariable Integer courseId){
        Integer userId = UserAuthUtil.getUserId();
        Courses courses = coursesService.getById(courseId);
        List<Classes> list = null;
        if(courses==null){
            return  Result.failed(ResultCode.PARAM_ERROR);
        }
        //老师获取班级列表;
        if(courses.getUserId()==userId){
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
            @Schema(description = "true:重新生成") @RequestParam(required = false) Boolean anew){
        //判断是不是我的课
        Classes myClass = classesService.getById(classId);
        if(myClass==null||myClass.getTeacherId()!=UserAuthUtil.getUserId()){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //班级码是否存在
        String code = null;
        if(!anew){
            code=classesService.getCodeById(classId);
        }
        //生成班级码
        if(code==null){
            code=classesService.generateCode(classId);
        }
        long validTime=classesService.getCodeValidTime(classId);
        log.info("班级码:"+code+"时间"+validTime);
        LocalDateTime localDateTime=LocalDateTime.now();
        //获取失效时间
        localDateTime=localDateTime.plusSeconds(validTime);
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
        //班级id查找课程
        Classes classes = classesService.getById(classId);
        JoinClass isJoinClass = joinClassService.getJoinByCourseId(userId, classes.getCourseId());
        if(isJoinClass!=null){
            return Result.msgInfo("请勿重复加入班级");
        }
        //TODO:禁止套娃（老师加入自己的课程）
        JoinClass joinClass=new JoinClass();
        joinClass.setClassId(classId);
        joinClass.setStudentId(userId);
        joinClassService.save(joinClass);
        return Result.msgSuccess("加入成功~");
    }
}
