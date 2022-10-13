package com.baymax.exam.feign;

import com.baymax.exam.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/9 19:30
 * @description：
 * @modified By：
 * @version:
 */
@FeignClient(value = "exam-user")
public interface UserServiceClient {
    @GetMapping("/findUser")
     User findUser(@RequestParam String username);
}
