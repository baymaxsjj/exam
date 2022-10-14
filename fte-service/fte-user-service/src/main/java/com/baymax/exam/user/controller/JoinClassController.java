package com.baymax.exam.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.model.JoinClass;
import com.baymax.exam.model.User;
import com.baymax.exam.user.service.impl.JoinClassServiceImpl;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "获取班级成员")
    @GetMapping("/{classId}/student/list")
    public Result getList(
            @Schema(description = "班级id") @PathVariable Integer classId,
            @RequestParam Long currentPage){
        JoinClass isJoinClass = joinClassService.getJoinByClassId(UserAuthUtil.getUserId(), classId);
        if(isJoinClass==null){
            return Result.msgError("未加入该课程班级");
        }
        IPage<User> classUsers = joinClassService.getClassUsers(classId, currentPage, 10);
        return Result.success(PageResult.setResult(classUsers));
    }
}
