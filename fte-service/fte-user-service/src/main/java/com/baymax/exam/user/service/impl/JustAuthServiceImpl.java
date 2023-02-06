package com.baymax.exam.user.service.impl;

import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthQqRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author ：Baymax
 * @date ：Created in 2022/3/13 10:30
 * @description：第三方登录
 * @modified By：
 * @version: 1.0
 */
@Service
public class JustAuthServiceImpl {
    @Value("${justauth.type.gitee.client-id}")
    private String giteeClientId;
    @Value("${justauth.type.gitee.client-secret}")
    private String giteeClientSecret;
    @Value("${justauth.type.gitee.redirect-uri}")
    private String giteeRedirectUri;

    @Value("${justauth.type.qq.client-id}")
    private String qqClientId;
    @Value("${justauth.type.qq.client-secret}")
    private String qqClientSecret;
    @Value("${justauth.type.qq.redirect-uri}")
    private String qqRedirectUri;

    @Value("${justauth.type.github.client-id}")
    private String githubClientId;
    @Value("${justauth.type.github.client-secret}")
    private String githubClientSecret;
    @Value("${justauth.type.github.redirect-uri}")
    private String githubRedirectUri;
    public AuthRequest getAuthRequest(String type) {
        AuthRequest authRequest;
        switch (type){
            case "github":
                authRequest=new AuthGithubRequest(AuthConfig.builder()
                        .clientId(githubClientId)
                        .clientSecret(githubClientSecret)
                        .redirectUri(githubRedirectUri)
                        .build());
                break;
            case "gitee":
                authRequest=new AuthGiteeRequest(AuthConfig.builder()
                        .clientId(giteeClientId)
                        .clientSecret(giteeClientSecret)
                        .redirectUri(giteeRedirectUri)
                        .build());
                break;
            case "qq":
            default:
                authRequest=new AuthQqRequest(AuthConfig.builder()
                        .clientId(qqClientId)
                        .clientSecret(qqClientSecret)
                        .redirectUri(qqRedirectUri)
                        .build());
        }
        return authRequest;
    }
}
