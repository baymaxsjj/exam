package com.baymax.exam.common.core.base;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/9 20:19
 * @description：Api接口前缀
 * @modified By：
 * @version:
 */
public interface  ExamAuth {
    /**
     * 内部api前缀
     */
    String API_INNER_PRE="/inner";
    /**
     * 公开api前缀
     */
    String API_OPEN_PRE="/open";
    /**
     * JWT存储权限前缀
     */
    String AUTHORITY_PREFIX = "ROLE_";

    /**
     * JWT存储权限属性
     */
    String AUTHORITY_CLAIM_NAME = "authorities";

    /**
     * 后台管理client_id
     */
    String ADMIN_CLIENT_ID = "admin-app";

    /**
     * 前台商城client_id
     */
    String PORTAL_CLIENT_ID = "exam-app";

    /**
     * Redis缓存权限规则key
     */
    String REDIS_ROLES_MAP_KEY = "auth:resourceRolesMap";
    /**
     * 考试答题key
     */
    String REDIS_SUBJECT_MAP_KEY="exam:userIdMap:";
    /**
     * 电子邮件代码key
     */
    String REDIS_EMAIL_CODE_KEY="email:code:";
    String REDIS_CLASS_CODE_KEY="class:code:";
    String REDIS_CODE_CLASS_KEY="code:class::";

    /**
     * 认证信息Http请求头
     */
    String JWT_TOKEN_HEADER = "Authorization";

    /**
     * JWT令牌前缀
     */
    String JWT_TOKEN_PREFIX = "Bearer ";
    String USER_TOKEN_HEADER="Token";
}
