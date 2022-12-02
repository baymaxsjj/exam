package com.baymax.exam.message.interceptor;

import cn.hutool.json.JSONUtil;
import com.baymax.exam.common.core.base.SecurityConstants;
import com.baymax.exam.common.core.base.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/23 16:00
 * @description：
 * @modified By：
 * @version:
 */
@Slf4j
@Component
public class MyWebSocketInterceptor implements HandshakeInterceptor {
    static String USER_INFO="user_info";
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String userStr=request.getHeaders().getFirst(SecurityConstants.USER_TOKEN_HEADER);
        LoginUser loginUser =  JSONUtil.toBean(userStr, LoginUser.class);
        attributes.put(USER_INFO,loginUser);
        log.info("用户信息："+loginUser);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
