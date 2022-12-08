package com.baymax.exam.user.controller;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/5 9:34
 * @description：用户信息
 * @modified By：
 * @version:
 */

import com.baymax.exam.common.core.base.SecurityConstants;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.user.model.User;
import com.baymax.exam.user.service.impl.UserServiceImpl;
import com.baymax.exam.web.annotation.Inner;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@Tag(name = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImpl userService;
    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    Result getUserInfo(){
        log.info("用户id"+UserAuthUtil.getUserId());
        return Result.success(userService.getById(UserAuthUtil.getUserId()));
    }
    @Operation(summary = "获取用户信息")
    @PostMapping("/update")
    Result updateUser(@RequestBody @Validated({User.UpdateUserRequestValid.class}) User user){
//        =userService.updateUser(user);
//        userService.up
        return Result.success(userService.getById(UserAuthUtil.getUserId()));
    }
    @Inner
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
