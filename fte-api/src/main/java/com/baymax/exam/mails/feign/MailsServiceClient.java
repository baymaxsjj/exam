package com.baymax.exam.mails.feign;

import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.mails.model.Mails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ：Baymax
 * @date ：Created in 2023/2/6 13:46
 * @description：
 * @modified By：
 * @version:
 */
@FeignClient(value = "exam-mails",path = "/mails")
public interface MailsServiceClient {
    @PostMapping("/send-mail")
    public Result<Boolean> sendMail(@RequestBody Mails mailVo);
}
