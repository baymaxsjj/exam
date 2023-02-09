package com.baymax.exam.user.feign;

import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.user.model.UserAuthInfo;
import com.baymax.exam.user.po.CourseUserPo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;

/**
 * @author ：Baymax
 * @date ：Created in 2022/12/1 9:52
 * @description：
 * @modified By：
 * @version:
 */
@FeignClient(value = "exam-user",contextId = "JoinClassClient",path = "/join-class")
public interface JoinClassClient {

     @PostMapping("/students/list")
     PageResult<UserAuthInfo> getBatchClassUser(
             @RequestBody @Validated CourseUserPo courseUser,
             @RequestParam Boolean isInlist,
             @RequestParam Long currPage,
             @RequestParam Long pageSize);
     @GetMapping("/{classId}/student/list")
     public Result<PageResult<UserAuthInfo>> getList(
             @PathVariable Integer classId,
             @RequestParam(required = false,defaultValue = "1") Long currentPage,
             @RequestParam(required = false,defaultValue = "10") Long pageSize);
     @PostMapping("/student/number")
     public Result<Long> getStudentNumberByIds(@RequestBody Collection<Integer> classIds);

}
