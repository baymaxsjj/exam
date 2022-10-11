package com.baymax.exam.user.controller;

import com.baymax.exam.model.User;
import com.baymax.exam.user.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/9 19:41
 * @description：远程调用接口
 * @modified By：
 * @version:
 */
@Tag(name = "用户类远程接口")
@RestController
public class RemoteController
{
    @Autowired
    UserServiceImpl userService;
    @GetMapping("/findUser")
    public User findUser( String username){
        User user = null;
        if(username.contains("@")){
            user=userService.getUserByEmail(username);
        }else{
            user=userService.getUserByUserName(username);
        }
        return user;
    }
}
