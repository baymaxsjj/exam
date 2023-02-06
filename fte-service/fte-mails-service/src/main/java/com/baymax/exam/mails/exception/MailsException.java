package com.baymax.exam.mails.exception;

/**
 * @author ：Baymax
 * @date ：Created in 2022/3/13 9:20
 * @description：邮件发送异常
 * @modified By：
 * @version: 1.0
 */
public class MailsException extends RuntimeException{
    public MailsException(String msg){
        super(msg);
    }
    public MailsException(Throwable cause){
        super(cause);
    }
}
