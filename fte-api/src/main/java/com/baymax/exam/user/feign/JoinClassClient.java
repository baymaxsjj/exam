package com.baymax.exam.user.feign;

import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.user.model.User;
import com.baymax.exam.user.po.CourseUserPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
     PageResult<User> getBatchClassUser(
             @RequestBody @Validated CourseUserPo courseUser,
             @RequestParam Boolean isInlist,
             @RequestParam Long currPage,
             @RequestParam Long pageSize);


}
