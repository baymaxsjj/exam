package com.baymax.exam.service.user_service.controller;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/5 9:34
 * @description：用户信息
 * @modified By：
 * @version:
 */

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "用户管理")
@RestController
@RequestMapping("/school/user")
public class UserController {
    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    String getUserInfo(){
        return "hello0";
    }
}
