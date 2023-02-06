package com.baymax.exam.mails.service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baymax.exam.mails.exception.MailsException;
import com.baymax.exam.mails.mapper.MailsMapper;
import com.baymax.exam.mails.model.Mails;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Baymax
 * @since 2022-03-13
 */
@Slf4j
@Service
public class MailsServiceImpl extends ServiceImpl<MailsMapper, Mails>{
    public Logger logger = LoggerFactory.getLogger(getClass());//提供日志类
    //邮件的发送者
    @Value("${spring.mail.username}")
    private String from;
    @Value("${spring.mail.nickname}")
    private String nickname;
    @Autowired
    public JavaMailSenderImpl mailSender;//注入邮件工具类
    @Autowired
    public MailsMapper mailsMapper;
    //发送邮件的模板引擎
    @Autowired
    private FreeMarkerConfigurer configurer;
    /**
     * 发送邮件
     */
    public boolean sendMail(Mails mailVo) {
        try {
            boolean istem=false;
            if(StringUtils.isEmpty(mailVo.getText())){
                log.info("模板：{}",mailVo.getTemplate());
                Template template = configurer.getConfiguration().getTemplate(mailVo.getTemplate());
                //赋值后的模板邮件内容
                String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailVo.getData());
                mailVo.setText(text);
                istem=true;
            }
            checkMail(mailVo); //1.检测邮件
            mailVo=sendMimeMail(mailVo); //2.发送邮件
            if(istem){
                mailVo.setText(mailVo.getData().toString());
            }
            saveMail(mailVo);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败:", e);//打印错误信息
            mailVo.setStatus("fail");
            mailVo.setError(e.getMessage());
            return false;
        }

    }

    //检测邮件信息类
    public void checkMail(Mails mailVo) {
        if (StringUtils.isEmpty(mailVo.getTo())) {
            throw new MailsException("邮件收信人不能为空");
        }
        if (StringUtils.isEmpty(mailVo.getSubject())) {
            throw new MailsException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(mailVo.getText())) {
            throw new MailsException("邮件内容不能为空");
        }
    }
    //构建复杂邮件信息类
    @Async
    public Mails sendMimeMail(Mails mailVo) {
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);//true表示支持复杂类型
            mailVo.setFrom(from);//邮件发信人从配置项读取
            messageHelper.setFrom(nickname+"<"+from+">");//邮件发信人
            messageHelper.setTo(mailVo.getTo().split(","));//邮件收信人
            messageHelper.setSubject(mailVo.getSubject());//邮件主题
            messageHelper.setText(mailVo.getText());
            if (!StringUtils.isEmpty(mailVo.getCc())) {//抄送
                messageHelper.setCc(mailVo.getCc().split(","));
            }
            if (!StringUtils.isEmpty(mailVo.getBcc())) {//密送
                messageHelper.setCc(mailVo.getBcc().split(","));
            }
            if (mailVo.getMultipartFiles() != null) {//添加邮件附件
                for (MultipartFile multipartFile : mailVo.getMultipartFiles()) {
                    messageHelper.addAttachment(multipartFile.getOriginalFilename(), multipartFile);
                }
            }
            long beforeT=System.currentTimeMillis();


            mailSender.send(messageHelper.getMimeMessage());//正式发送邮件
            long afterT=System.currentTimeMillis();
            logger.info("邮件发送时间:"+(afterT-beforeT));
            mailVo.setStatus("ok");
            logger.info("发送邮件成功：{}->{}", mailVo.getFrom(), mailVo.getTo());
            return mailVo;
        } catch (Exception e) {
            throw new MailsException(e);//发送失败
        }
    }
    @Async
    //保存邮件
    public Mails saveMail(Mails mailVo) {
        save(mailVo);
        return mailVo;
    }
}
