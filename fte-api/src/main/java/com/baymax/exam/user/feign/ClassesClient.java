package com.baymax.exam.user.feign;

import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.user.model.Courses;
import com.baymax.exam.user.model.JoinClass;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ：Baymax
 * @date ：Created in 2023/1/31 17:09
 * @description：
 * @modified By：
 * @version:
 */
/**
 * @author ：Baymax
 * @date ：Created in 2022/12/1 10:07
 * @description：
 * @modified By：
 * @version:
 */
@FeignClient(value = "exam-user",contextId = "ClassesClient",path = "/classes")
public interface ClassesClient {
    @GetMapping("/info/{classId}")
    public Result classInfo(@PathVariable String classId);
}
