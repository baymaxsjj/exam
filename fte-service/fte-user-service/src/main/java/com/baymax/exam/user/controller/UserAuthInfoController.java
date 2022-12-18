package com.baymax.exam.user.controller;

import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.user.model.UserAuthInfo;
import com.baymax.exam.user.service.impl.UserAuthInfoServiceImpl;
import com.baymax.exam.web.annotation.Inner;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * VIEW 前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-12-14
 */
@RestController
@RequestMapping("/user-auth")
public class UserAuthInfoController {
    @Autowired
    UserAuthInfoServiceImpl userAuthInfoService;
    @Inner
    @Operation(summary = "学生认证信息")
    @GetMapping("/info/{userId}")
    public UserAuthInfo getAuthInfo(@PathVariable Integer userId){
        return userAuthInfoService.getStudentByUserId(userId);
    }
    @Operation(summary = "学生认证信息")
    @GetMapping("/info")
    public Result getAuthInfo(){
        Integer userId = UserAuthUtil.getUserId();
        return Result.success(userAuthInfoService.getStudentByUserId(userId));
    }
}
