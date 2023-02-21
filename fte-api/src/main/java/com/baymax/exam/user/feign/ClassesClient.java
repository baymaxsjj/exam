package com.baymax.exam.user.feign;

import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.user.model.Classes;
import com.baymax.exam.user.model.Courses;
import com.baymax.exam.user.model.JoinClass;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

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
    public Result<Classes> classInfo(@PathVariable String classId);

    @PostMapping("/{courseId}/part/list")
    public Result<List<Classes>> getClassListByIds(
            @RequestBody Collection<Integer> classIds,
            @PathVariable Integer courseId
    );
    @PostMapping("/{courseId}/user/class")
    public Classes getClassByUserId(
            @PathVariable Integer courseId,
            @RequestParam Integer userId);
}
