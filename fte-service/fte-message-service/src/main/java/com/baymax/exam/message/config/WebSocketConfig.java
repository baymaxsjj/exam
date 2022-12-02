package com.baymax.exam.message.config;

import com.baymax.exam.message.interceptor.MyWebSocketHandler;
import com.baymax.exam.message.interceptor.MyWebSocketInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.Resource;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/21 20:55
 * @description：WebSocket配置类
 * @modified By：
 * @version:
 */
@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer{
    @Resource
    MyWebSocketInterceptor myWebSocketInterceptor;

    @Resource
    MyWebSocketHandler myWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(myWebSocketHandler, "/socket")
                .addInterceptors(myWebSocketInterceptor)
                .setAllowedOrigins("*");
    }
}

