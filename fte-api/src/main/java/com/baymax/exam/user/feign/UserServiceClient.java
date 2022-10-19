package com.baymax.exam.user.feign;

import com.baymax.exam.user.model.Courses;
import com.baymax.exam.user.model.JoinClass;
import com.baymax.exam.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/9 19:30
 * @description：
 * @modified By：
 * @version:
 */
@FeignClient(value = "exam-user",path = "/inner")
public interface UserServiceClient {
    @GetMapping("/findUser")
     User findUser(@RequestParam String username);

    @GetMapping("/findCourse")
    public Courses findCourse(@RequestParam Integer courseId);

    @GetMapping("/joinCourseByStuId")
    public JoinClass joinCourseByStuId(@RequestParam Integer courseId,@RequestParam Integer stuId);
}
