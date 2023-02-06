package com.baymax.exam.user.controller;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/5 9:34
 * @description：用户信息
 * @modified By：
 * @version:
 */

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baymax.exam.common.core.exception.ResultException;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.file.feign.FileDetailClient;
import com.baymax.exam.user.model.User;
import com.baymax.exam.user.service.impl.UserAuthInfoServiceImpl;
import com.baymax.exam.user.service.impl.UserServiceImpl;
import com.baymax.exam.web.annotation.Inner;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Validated
@Tag(name = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    UserAuthInfoServiceImpl studentInfoService;
    @Autowired
    FileDetailClient fileDetailClient;
    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    Result getUserInfo(){
        log.info("用户id"+UserAuthUtil.getUserId());
        Integer userId = UserAuthUtil.getUserId();
        return Result.success(studentInfoService.getStudentByUserId(userId));
    }
    @Operation(summary = "获取用户信息")
    @GetMapping("/base/info")
    Result<User> getBaseUserInfo(){
        log.info("用户id"+UserAuthUtil.getUserId());
        Integer userId = UserAuthUtil.getUserId();
        return Result.success(userService.getById(userId));
    }
    @Operation(summary = "更新头像")
    @PostMapping("/upload-avatar")
    Result<String> uploadAvatar(@RequestPart("file") MultipartFile file) throws ResultException {
        Integer userId = UserAuthUtil.getUserId();
        Result<String> result = fileDetailClient.uploadImage(file, "/"+userId+"/avatar/", userId.toString(), "user");
        String url = result.getResultDate();
        LambdaUpdateWrapper<User> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId,userId);
        updateWrapper.set(User::getPicture,url);
        userService.update(updateWrapper);
        return Result.success("更新成功",url);
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
    @Inner
    @Operation(summary = "用户名/邮箱获取用户信息")
    @PostMapping ("/batchUser")
    List<User> getBatchUser(@RequestBody List<Integer> ids){
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(User::getId,ids);
        return userService.list(queryWrapper);
    };
    @Operation(summary = "信息认证")
    @PostMapping("/authentication")
    Result  authentication(){
        return Result.success();
    }

}
