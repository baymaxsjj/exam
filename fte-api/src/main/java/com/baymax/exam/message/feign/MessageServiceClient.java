package com.baymax.exam.message.feign;

import com.baymax.exam.message.MessageResult;
import com.baymax.exam.user.model.Courses;
import com.baymax.exam.user.model.JoinClass;
import com.baymax.exam.user.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/9 19:30
 * @description：
 * @modified By：
 * @version:
 */
@FeignClient(value = "exam-message",path = "/message-info")
public interface MessageServiceClient {
    @PostMapping("/inner/send-message")
    Boolean sendMessage(@RequestBody @Validated MessageResult message);
    @PostMapping("/inner/batch/send-message")
    Boolean sendBatchMessage(@RequestBody @Validated MessageResult message);
}
