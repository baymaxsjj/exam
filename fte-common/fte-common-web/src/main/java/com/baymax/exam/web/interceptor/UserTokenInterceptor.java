package com.baymax.exam.web.interceptor;

import cn.hutool.json.JSONUtil;
import com.baymax.exam.common.core.base.SecurityConstants;
import com.baymax.exam.common.core.base.LoginUser;
import com.baymax.exam.web.utils.UserAuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/12 17:47
 * @description：获取用户拦截器
 * @modified By：
 * @version:
 */
@Component
@Slf4j
public class  UserTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userStr=request.getHeader(SecurityConstants.USER_TOKEN_HEADER);
        String ip=request.getHeader(SecurityConstants.REQUEST_HEADER_KEY_CLIENT_REAL_IP);
        //未登录的公开接口
        log.info("用户信息："+userStr);
        if(userStr==null){
            return true;
        }
        //获取用户信息
        LoginUser loginUser = JSONUtil.toBean(userStr, LoginUser.class);
        loginUser.setIp(ip);
        UserAuthUtil.add(loginUser);
        log.info("用户信息："+loginUser);
        return true;
    }

}
