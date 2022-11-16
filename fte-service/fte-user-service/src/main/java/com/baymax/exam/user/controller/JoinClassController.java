package com.baymax.exam.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.user.model.Classes;
import com.baymax.exam.user.model.JoinClass;
import com.baymax.exam.user.model.User;
import com.baymax.exam.user.service.impl.ClassesServiceImpl;
import com.baymax.exam.user.service.impl.JoinClassServiceImpl;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.lettuce.core.codec.RedisCodec;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-10-13
 */
@Tag(name = "加入课程班级")
@RestController
@RequestMapping("/join-class")
public class JoinClassController {
    @Autowired
    JoinClassServiceImpl joinClassService;
    @Autowired
    ClassesServiceImpl classesService;
    @Operation(summary = "获取班级成员")
    @GetMapping("/{classId}/student/list")
    public Result getList(
            @Schema(description = "班级id") @PathVariable Integer classId,
            @RequestParam(required = false,defaultValue = "1") Long currentPage,
            @RequestParam(required = false,defaultValue = "10") Long pageSize){
        Classes classes = classesService.getById(classId);
        Integer userId = UserAuthUtil.getUserId();
        if(classes==null){
            return Result.failed(ResultCode.PARAM_ERROR);
        }
        if(classes.getTeacherId()!=userId){
            JoinClass isJoinClass = joinClassService.getJoinByClassId(userId, classId);
            if(isJoinClass==null){
                return Result.msgError("未加入该课程班级");
            }
        }
        IPage<User> classUsers = joinClassService.getClassUsers(classId, currentPage, pageSize);
        return Result.success(PageResult.setResult(classUsers));
    }
    @Operation(summary = "获取班级人数")
    @PostMapping("/student/batch/list")
    public Result getQuantity(
            @RequestBody Set<Integer> classIds,
                    @RequestParam(required = false,defaultValue = "1") Long currentPage,
            @RequestParam(required = false,defaultValue = "10") Long pageSize){
        Integer userId = UserAuthUtil.getUserId();
        final IPage<User> batchClassUsers = joinClassService.getBatchClassUsers(classIds, userId, currentPage, pageSize);
        return Result.success(PageResult.setResult(batchClassUsers));
    }

}
