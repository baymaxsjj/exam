package com.baymax.exam.mails.controller;

import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.mails.model.Mails;
import com.baymax.exam.mails.service.MailsServiceImpl;
import com.baymax.exam.web.annotation.Inner;
import org.hibernate.validator.constraints.ru.INN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：Baymax
 * @date ：Created in 2023/2/6 13:41
 * @description：
 * @modified By：
 * @version:
 */
@RestController
@RequestMapping("/mails")
public class MailsController {
    @Autowired
    MailsServiceImpl mailsService;
    @Inner
    @PostMapping("/send-mail")
    public Result sendMail(@RequestBody Mails mailVo) {
        return Result.success(mailsService.sendMail(mailVo));
    }
}
