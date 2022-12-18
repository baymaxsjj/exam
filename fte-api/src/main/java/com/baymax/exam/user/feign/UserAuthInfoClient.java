package com.baymax.exam.user.feign;

import com.baymax.exam.user.model.UserAuthInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ：Baymax
 * @date ：Created in 2022/12/14 9:28
 * @description：学生信息
 * @modified By：
 * @version:
 */
@FeignClient(value = "exam-user",contextId = "UserAuthInfoClient",path = "/user-auth")
public interface UserAuthInfoClient {
    @GetMapping("/info/{userId}")
    public UserAuthInfo getStudentInfo(@PathVariable Integer userId);
}
