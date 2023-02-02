package com.baymax.exam.message.interceptor;

import cn.hutool.json.JSONUtil;
import com.baymax.exam.common.core.base.LoginUser;
import com.baymax.exam.common.core.enums.ClientIdEnum;
import com.baymax.exam.message.MessageResult;
import com.baymax.exam.message.model.MessageInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/23 16:21
 * @description：
 * @modified By：
 * @version:
 */
@Slf4j
@Component
public class MyWebSocketHandler implements WebSocketHandler {
    private static final Map<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<>();
    /**
     * 建立连接
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 缓存用户信息: userInfo
        String terminalId = getTerminalId(session);
         if(CLIENTS.containsKey(terminalId)){
             CLIENTS.get(terminalId).close();
         }
         CLIENTS.put(terminalId,session);
        log.info(session.getUri().getPath() + "，打开连接完成：" + session.getId());
    }

    /**
     * 接收消息
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
    }

    /**
     * 发生错误
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // 清除用户缓存信息
        session.close();
    }

    /**
     * 关闭连接
     * @param session
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // 清除用户缓存信息
        String terminalId = getTerminalId(session);
        CLIENTS.remove(terminalId);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    private String getTerminalId(WebSocketSession session){
        LoginUser loginUser = (LoginUser) session.getAttributes().get(MyWebSocketInterceptor.USER_INFO);
        return getTerminalId(loginUser.getClientId(),loginUser.getId());
    }

    private String getTerminalId(ClientIdEnum clientId, Integer userId){
        String terminalId=clientId.getValue()+":"+userId;
        log.info("用户标识："+terminalId);
        return terminalId;
    }
    public boolean sendMessage(String terminalId,String msg) {
        if(!CLIENTS.containsKey(terminalId)){
            return false;
        }
        WebSocketSession session=CLIENTS.get(terminalId);
        TextMessage textMessage=new TextMessage(msg);
        try {
            if(session.isOpen()){
                session.sendMessage(textMessage);
            }
            log.info("推送成功：" + msg);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("推送异常：" + e);
            return false;
        }

    }

    public boolean sendMessage(MessageResult result) {
        MessageInfo info = result.getInfo();
        String terminalId=getTerminalId(info.getClientId(),info.getUserId());
        try {
            String msg = JSONUtil.toJsonStr(result);
            sendMessage(terminalId,msg);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 批处理消息,改客户端内全体推送
     *
     * @param result 结果
     * @return boolean
     */
    public void batchMessage(MessageResult result){
        String msg = JSONUtil.toJsonStr(result);
        final ClientIdEnum clientId = result.getInfo().getClientId();

        CLIENTS.forEach((key,value)->{
            if(key.startsWith(clientId.getValue())){
                boolean flag = sendMessage(key, msg);
            }
        });
    }

    /**
     * 批处理消息，用户ids 推送
     *
     * @param userIds 用户id
     * @param result  结果
     * @return boolean
     */
    public Map<Integer,Boolean> batchMessage(Set<Integer> userIds, MessageResult result) throws JsonProcessingException {
        String msg = new ObjectMapper().writeValueAsString(result);
        final ClientIdEnum clientId = result.getInfo().getClientId();
        Map<Integer,Boolean> sendResult=new HashMap<>();
        userIds.forEach(userId -> {
            String terminalId = getTerminalId(clientId, userId);
            sendResult.put(userId,sendMessage(terminalId,msg));
        });
        return sendResult;
    }
}
