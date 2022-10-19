package com.baymax.exam.web.interceptor;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baymax.exam.common.core.base.ExamAuth;
import com.baymax.exam.common.core.base.LoginUser;
import com.baymax.exam.web.utils.UserAuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
        String userStr=request.getHeader(ExamAuth.USER_TOKEN_HEADER);
        log.info(userStr);
        //未登录的公开接口
        if(userStr==null){
            return true;
        }
        //获取用户信息
        LoginUser loginUser = JSONUtil.toBean(userStr, LoginUser.class);
        UserAuthUtil.add(loginUser);
        return true;
    }
}
