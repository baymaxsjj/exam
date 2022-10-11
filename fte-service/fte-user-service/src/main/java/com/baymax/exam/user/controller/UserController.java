package com.baymax.exam.user.controller;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/5 9:34
 * @description：用户信息
 * @modified By：
 * @version:
 */

import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.core.result.ResultCode;
import com.baymax.exam.model.User;
import com.baymax.exam.user.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "用户管理")
@RestController
public class UserController {
    @Autowired
    UserServiceImpl userService;
    @Operation(summary = "获取用户信息")
    @GetMapping("/user/info")
    Result getUserInfo(){
        return Result.success("hello0");
    }
    @Operation(summary = "注册信息")
    @PostMapping("/register")
    Result register(@RequestBody @Validated({User.RegisterRequestValid.class}) User user){
        // TODO: 邮箱验证码校验
        return userService.addUser(user);
    }
    @Operation(summary = "忘记密码")
    @PostMapping("/forgotPassword")
    Result  forgotPassword(){
        return Result.success();
    }
    @Operation(summary = "信息认证")
    @PostMapping("/authentication")
    Result  authentication(){
        return Result.success();
    }

}
