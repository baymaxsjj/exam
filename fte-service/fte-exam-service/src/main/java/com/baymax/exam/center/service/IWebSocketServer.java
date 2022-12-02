package com.baymax.exam.center.service;

import javax.websocket.Session;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/18 14:48
 * @description：
 * @modified By：
 * @version:
 */
public interface IWebSocketServer {

    /**
     * 连接建立成功调用
     *
     * @param session
     */
    void onOpen(Session session, String userId);

    /**
     * 收到客户端消息后调用
     *
     * @param message
     * @param session
     */
    void onMessage(String message, Session session);

    /**
     * 连接关闭调用
     */
    void onClose();

    /**
     * 发生错误/异常时调用
     *
     * @param session
     * @param error
     */
    void onError(Session session, Throwable error);
}
