package com.baymax.exam.gateway.authorization;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baymax.exam.auth.model.LoginUser;
import com.baymax.exam.common.core.base.ExamAuth;
import com.nimbusds.jose.JWSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/11 19:36
 * @description：鉴权管理器，用于判断是否有资源的访问权限
 * @modified By：
 * @version:
 */
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        URI uri = request.getURI();
        PathMatcher pathMatcher = new AntPathMatcher();
        //公开直接放行
        if (pathMatcher.match(ExamAuth.API_OPEN_PRE+"/**", uri.getPath())) {
            return Mono.just(new AuthorizationDecision(true));
        }
        //对应跨域的预检请求直接放行
        if(request.getMethod()== HttpMethod.OPTIONS){
            return Mono.just(new AuthorizationDecision(true));
        }
        //内部接口禁止访问
        if (pathMatcher.match(ExamAuth.API_INNER_PRE+"/**", uri.getPath())) {
            return Mono.just(new AuthorizationDecision(false));
        }
        //不同用户体系登录不允许互相访问
        try {
            String token = request.getHeaders().getFirst(ExamAuth.JWT_TOKEN_HEADER);
            if(StrUtil.isEmpty(token)){
                return Mono.just(new AuthorizationDecision(false));
            }
            String realToken = token.replace(ExamAuth.JWT_TOKEN_PREFIX, "");
            JWSObject jwsObject = JWSObject.parse(realToken);
            String userStr = jwsObject.getPayload().toString();
            LoginUser loginUser= JSONUtil.toBean(userStr, LoginUser.class);
            if (ExamAuth.ADMIN_CLIENT_ID.equals(loginUser.getClientId()) && !pathMatcher.match(ExamAuth.API_ADMIN_PRE+"/**", uri.getPath())) {
                return Mono.just(new AuthorizationDecision(false));
            }
            if (ExamAuth.PORTAL_CLIENT_ID.equals(loginUser.getClientId()) && pathMatcher.match(ExamAuth.API_USER_PRE+"/**", uri.getPath())) {
                return Mono.just(new AuthorizationDecision(false));
            }
        } catch ( ParseException e) {
            e.printStackTrace();
            return Mono.just(new AuthorizationDecision(false));
        }
        //非管理端路径直接放行
        if (!pathMatcher.match(ExamAuth.API_ADMIN_PRE, uri.getPath())) {
            return Mono.just(new AuthorizationDecision(true));
        }
        return Mono.just(new AuthorizationDecision(false));
//        //管理端路径需校验权限
//        Map<Object, Object> resourceRolesMap = redisTemplate.opsForHash().entries(ExamAuth.RESOURCE_ROLES_MAP_KEY);
//        Iterator<Object> iterator = resourceRolesMap.keySet().iterator();
//        List<String> authorities = new ArrayList<>();
//        while (iterator.hasNext()) {
//            String pattern = (String) iterator.next();
//            if (pathMatcher.match(pattern, uri.getPath())) {
//                authorities.addAll(Convert.toList(String.class, resourceRolesMap.get(pattern)));
//            }
//        }
//        authorities = authorities.stream().map(i -> i = ExamAuth.AUTHORITY_PREFIX + i).collect(Collectors.toList());
//        //认证通过且角色匹配的用户可访问当前路径
//        return mono
//                .filter(Authentication::isAuthenticated)
//                .flatMapIterable(Authentication::getAuthorities)
//                .map(GrantedAuthority::getAuthority)
//                .any(authorities::contains)
//                .map(AuthorizationDecision::new)
//                .defaultIfEmpty(new AuthorizationDecision(false));
    }

}
