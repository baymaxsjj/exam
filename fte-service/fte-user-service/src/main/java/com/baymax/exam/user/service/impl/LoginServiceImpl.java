package com.baymax.exam.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baymax.exam.common.core.base.RedisKeyConstants;
import com.baymax.exam.common.core.exception.ResultException;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.common.redis.utils.RedisUtils;
import com.baymax.exam.mails.feign.MailsServiceClient;
import com.baymax.exam.mails.model.Mails;
import com.baymax.exam.user.enums.LoginTypeEnum;
import com.baymax.exam.user.mapper.LoginMangerMapper;
import com.baymax.exam.user.model.LoginManger;
import com.baymax.exam.user.model.User;
import com.baymax.exam.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Baymax
 * @since 2022-03-12
 */
@Service
@Slf4j
public class LoginServiceImpl extends ServiceImpl<LoginMangerMapper, LoginManger> {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    MailsServiceClient mailsServiceClient;
    @Autowired
    JustAuthServiceImpl justAuthService;
    @Value("${exam.email.interval}")
    int interval;
    @Value("${exam.email.aging}")
    int emailAging;
    @Value("${exam.front.host}")
    String host;

    public String getRedisEmailCodeKey(String email){
        return RedisKeyConstants.REDIS_EMAIL_CODE_KEY+email;
    }

    /**
     * 发送验证码
     * @param email
     * @return
     */
    public Result sendEmailCode(String email) throws ResultException {
        boolean hasKey=redisUtils.hasKey(email);
        if(hasKey){
            long expire=redisUtils.getExpire(email);
            long time=emailAging-expire;
            if(time<interval){
                return Result.msgInfo("请"+(interval-time)+"秒后重新发送");
            }
        }
        int code= RandomUtil.randomInt(1000,9999);
        redisUtils.setCacheObject(getRedisEmailCodeKey(email),code,emailAging, TimeUnit.SECONDS);
        Mails mails=new Mails();
        mails.setTo(email);
        mails.setSubject("验证码");
        mails.setTemplate("EmailCode.html");
        Map data=new HashMap();
        data.put("code",code);
        mails.setData(data);
        Result<Boolean> result = mailsServiceClient.sendMail(mails);
         Boolean resultDate = result.getResultDate();
         if(resultDate){
             return Result.msgSuccess("验证码发送成功！");
         }else{
             return Result.msgError("验证码发送失败~");
         }
    }
    public  LoginManger getLoginByTypeAndUid(LoginTypeEnum loginTypeEnum,String loginId){
        LambdaQueryWrapper<LoginManger> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(LoginManger::getLoginType,loginTypeEnum);
        queryWrapper.eq(LoginManger::getLoginId,loginId);
        queryWrapper.last("limit 1");
        return getOne(queryWrapper);
    }

    public void authLogin(LoginTypeEnum type, AuthCallback callback, HttpServletRequest request, HttpServletResponse response){
        AuthRequest authRequest =  justAuthService.getAuthRequest(type.getType());
        AuthResponse<AuthUser> res;
        HttpSession session=request.getSession();
        Object bindId=session.getAttribute("id");
        session.removeAttribute("id");
            res= authRequest.login(callback);
            AuthUser authUser=res.getData();
            String avatar = authUser.getAvatar();
            String username = authUser.getUsername();
            String uuid = authUser.getUuid();
            LoginManger loginUser = getLoginByTypeAndUid(type, uuid);
            User user;
            String redirect;
//            没有绑定过账号
            if(loginUser==null){
                user=new User();
//                如果不是添加绑定，就创建账号
                user.setEncodePassword(RandomUtil.randomString(10));
                if(bindId==null){
                    String name=type+"_"+uuid+"_"+RandomUtil.randomString(4);
                    user.setUsername(name);
                    user.setNickname(username);
                    user.setPicture(avatar);
                    userService.save(user);
                }else{
                    user.setId((Integer) bindId);
                }
                user.setEnable(true);
//                绑定第三方登录
                LoginManger login=new LoginManger();
                login.setUserId(user.getId());
                login.setLoginId(uuid);
                login.setLoginType(type);
                save(login);
            }else{
//                已经绑定过，获取用户
                user=userService.getById(loginUser.getUserId());
            }
//            用户是否启用
            if(user.getEnable()){
                String token="";
                redirect=host+"?token="+token;
            }else if(bindId!=null) {
//                跳转到信息提示页面
                redirect=host+"?msg=绑定成功";
            }else{
                redirect=host+"?msg=绑定失败";
            }
            try {
                response.sendRedirect(redirect);
//                response.sendRedirect(errorPage+"?"+ResultCode.INTERFACE_REQUEST_TIMEOUT.toParams());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


}
