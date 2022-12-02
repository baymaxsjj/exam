package com.baymax.exam.message.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpoint;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/18 14:37
 * @description：考试控制台stocket服务
 * @modified By：
 * @version:
 */
@Slf4j
@Component
@ServerEndpoint(value = "/socket",subprotocols = {"protocol"})
public class MessageWebSocketService {
    ServerHttpRequest request;
//    /**
//     * 保存连接信息
//     */
//    private static final Map<String, Session> CLIENTS = new ConcurrentHashMap<>();
//    private String terminalId;
//    @OnOpen
//    public void onOpen(Session session) throws Exception {
//        log.info(session.getRequestURI().getPath() + "，打开连接开始：" + session.getId());
//        terminalId = getTerminalId(request);
//        //当前连接已存在，关闭
//        if (CLIENTS.containsKey(terminalId)) {
//            close(CLIENTS.get(terminalId));
//        }
//        CLIENTS.put(terminalId, session);
//
//        log.info(session.getRequestURI().getPath() + "，打开连接完成：" + session.getId());
//    }
//
//    @OnClose
//    public void onClose( Session session) throws Exception {
//        log.info(session.getRequestURI().getPath() + "，关闭连接开始：" + session.getId());
//        CLIENTS.remove(getTerminalId(request));
//        log.info(session.getRequestURI().getPath() + "，关闭连接完成：" + session.getId());
//    }
//
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        log.info("前台发送消息：" + message);
//    }
//
//    @OnError
//    public void onError(Session session, Throwable error) {
//        log.error(error.toString());
//    }
//    private String getTerminalId(ServerHttpRequest request){
//        String userStr=request.getHeaders().getFirst(ExamAuth.USER_TOKEN_HEADER);
//        LoginUser loginUser = JSONUtil.toBean(userStr, LoginUser.class);
//        return getTerminalId(loginUser.getClientId(),loginUser.getId());
//    }
//
//    private String getTerminalId(String clientId,Integer userId){
//        String terminalId=clientId+":"+userId;
//        log.info("用户标识："+terminalId);
//        return terminalId;
//    }
//    public void close(Session session) {
//        //判断当前连接是否在线
//        //        if (!session.isOpen()) {
//        //            return;
//        //        }
//
//        try {
//            session.close();
//        } catch (IOException e) {
//            log.error("关闭连接异常：" + e);
//        }
//    }
//
//    public void sendMessage(MessageResult result, Session session) {
//        try {
//            String message = JSONObject.toJSONString(result);
//            session.getAsyncRemote().sendText(message);
//            log.info("推送成功：" + message);
//        } catch (Exception e) {
//            log.error("推送异常：" + e);
//        }
//    }
//
//    public boolean sendMessage(MessageResult result) {
//        MessageInfo info = result.getInfo();
//        String terminalId=getTerminalId(info.getClientId(),info.getUserId());
//        try {
//            String message = JSONObject.toJSONString(result);
//            Session session = CLIENTS.get(terminalId);
//            if(session.isOpen()){
//                session.getAsyncRemote().sendText(message);
//            }
//            log.info("推送成功：" + message);
//            return true;
//        } catch (Exception e) {
//            log.error("推送异常：" + e);
//            return false;
//        }
//    }
//
//    /**
//     * 批处理消息,改客户端内全体推送
//     *
//     * @param result 结果
//     * @return boolean
//     */
//    public boolean batchMessage(MessageResult result){
//        String message = JSONObject.toJSONString(result);
//        String clientId = result.getInfo().getClientId();
//        CLIENTS.forEach((key,value)->{
//            if(key.startsWith(clientId)){
//                if(value.isOpen()){
//                    value.getAsyncRemote().sendText(message);
//                }
//            }
//        });
//        return true;
//    }
//
//    /**
//     * 批处理消息，用户ids 推送
//     *
//     * @param userIds 用户id
//     * @param result  结果
//     * @return boolean
//     */
//    public boolean batchMessage(Set<Integer> userIds, MessageResult result){
//        String message = JSONObject.toJSONString(result);
//        String clientId = result.getInfo().getClientId();
//        userIds.forEach(userId -> {
//             String terminalId = getTerminalId(clientId, userId);
//
//             if(CLIENTS.containsKey(terminalId)){
//                 Session client=CLIENTS.get(terminalId);
//                 if(client.isOpen()){
//                     client.getAsyncRemote().sendText(message);
//                 }
//             }
//        });
//        return true;
//    }
}
