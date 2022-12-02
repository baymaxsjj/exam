package com.baymax.exam.center.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baymax.exam.center.model.Tags;
import com.baymax.exam.center.service.impl.TagsServiceImpl;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.user.feign.CourseClient;
import com.baymax.exam.user.feign.UserClient;
import com.baymax.exam.user.model.Courses;
import com.baymax.exam.user.model.JoinClass;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 题目标签表 前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-10-17
 */
@Slf4j
@Validated
@Tag(name = "题目分组")
@RestController
@RequestMapping("/tags")
public class TagsController {
    @Autowired
    TagsServiceImpl tagsService;
    @Autowired
    CourseClient courseClient;

    @Operation(summary = "创建/更新题目分类")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated Tags tags){
        Courses course = courseClient.findCourse(tags.getCourseId());
        Integer userId = UserAuthUtil.getUserId();
        if(course==null||course.getUserId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //判断父标签是不是自己/课程
        if(tags.getParentId()!=null){
            Tags parent = tagsService.getById(tags.getParentId());
            if(parent==null||parent.getTeacherId()!=userId||parent.getCourseId()!=tags.getCourseId()){
                return Result.failed(ResultCode.PARAM_ERROR);
            }
        }
        String info="创建成功";
        //防止小人
        if(tags.getId()!=null){
            Tags tag = tagsService.getById(tags.getId());
            if(tag==null||tag.getTeacherId()!=userId){
                return Result.failed(ResultCode.PARAM_ERROR);
            }
            //禁止转移到到其他课程
            tags.setCourseId(null);
            info="更新成功";
        }
        tags.setTeacherId(userId);
        tagsService.saveOrUpdate(tags);
        return Result.msgSuccess(info);
    }
    @Operation(summary = "题库分类")
    @GetMapping("/{courseId}/list")
    public Result<List<Tags>> list(
            @Schema(description = "课程id") @PathVariable Integer courseId,
            @Schema(description = "父标签id") @RequestParam(required = false) Integer parentId){
        Courses course = courseClient.findCourse(courseId);
        Integer userId = UserAuthUtil.getUserId();
        if(course==null){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //老师
        if(course.getUserId()==userId){
            LambdaQueryWrapper<Tags> queryWrapper=new LambdaQueryWrapper();
            Map<SFunction<Tags,?>,Object> queryMap=new HashMap();
            queryMap.put(Tags::getCourseId,courseId);
            queryMap.put(Tags::getTeacherId,userId);
            queryMap.put(Tags::getParentId,parentId);
            queryWrapper.allEq(queryMap);
            return Result.success(tagsService.list(queryWrapper));
        }else{
            //1.判断是否课程班级中
            JoinClass joinClass = courseClient.joinCourseByStuId(courseId, userId);
            if(joinClass==null){
                return Result.failed(ResultCode.PARAM_ERROR);
            }
            //2.查找有公开题目的分类
            return Result.success(tagsService.getCoursePublicTags(courseId,parentId));
        }
    }

    @Operation(summary = "删除题目分类")
    @PostMapping("/delete/{tagId}")
    public Result delete(@PathVariable String tagId){
        Tags tag = tagsService.getById(tagId);
        Integer userId = UserAuthUtil.getUserId();
        if(tag==null||tag.getTeacherId()!=userId){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        //删除子分类
        LambdaQueryWrapper<Tags> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Tags::getParentId,tagId);
        tagsService.remove(queryWrapper);
        //删除自己
        tagsService.removeById(tagId);
        //TODO: 删除关联题目
        return Result.msgSuccess("删除成功");
    }
}
