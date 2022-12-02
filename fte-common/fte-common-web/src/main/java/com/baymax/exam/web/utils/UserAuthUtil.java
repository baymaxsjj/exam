package com.baymax.exam.web.utils;

import com.baymax.exam.common.core.base.LoginUser;

import java.util.function.IntSupplier;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/12 17:49
 * @description：用户上下文
 * @modified By：
 * @version:
 */
public final class UserAuthUtil {
    private static final ThreadLocal<LoginUser> user = new ThreadLocal<LoginUser>();

    public static void add(LoginUser loginUser) {
        user.set(loginUser);
    }

    public static void remove() {
        user.remove();
    }

    /**
     * 得到用户id
     *
     * @return
     */
    public static Integer getUserId() {
        return user.get().getId();
    }

    /**
     * 获取用户ip
     *
     * @return {@link String}
     */
    public static String getUserIp() {
        return user.get().getIp();
    }

    /**
     * 获取用户
     *
     * @return {@link LoginUser}
     */
    public static LoginUser getUser() {
        return user.get();
    }
}
