package com.baymax.exam.gateway.authorization;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baymax.exam.common.core.base.LoginUser;
import com.baymax.exam.common.core.base.SecurityConstants;
import com.baymax.exam.common.core.enums.ClientIdEnum;
import com.baymax.exam.gateway.config.IgnoreUrlsConfig;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.text.ParseException;
import java.util.List;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/11 19:36
 * @description：鉴权管理器，用于判断是否有资源的访问权限
 * @modified By：
 * @version:
 */
@Slf4j
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;
    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        final long sTime = System.currentTimeMillis();
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        URI uri = request.getURI();
        log.info("认证路径："+uri.getPath());
        PathMatcher pathMatcher = new AntPathMatcher();
        //白名单路径直接放行
        List<String> ignoreUrls = ignoreUrlsConfig.getUrls();
        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, uri.getPath())) {
                return Mono.just(new AuthorizationDecision(true));
            }
        }
        //公开接口直接放行
        if (pathMatcher.match("/*/"+ SecurityConstants.API_OPEN_PRE+"/**", uri.getPath())) {
            return Mono.just(new AuthorizationDecision(true));
        }
        //对应跨域的预检请求直接放行
        if(request.getMethod()== HttpMethod.OPTIONS){
            return Mono.just(new AuthorizationDecision(true));
        }
        //内部接口禁止访问
        if (pathMatcher.match("/*/"+ SecurityConstants.API_INNER_PRE+"/**", uri.getPath())) {
            return Mono.just(new AuthorizationDecision(false));
        }
        //不同用户体系登录不允许互相访问
        try {
            String token = request.getHeaders().getFirst(SecurityConstants.JWT_TOKEN_HEADER);
            //也可以在url加入token字段
            if(token==null){
                token=request.getQueryParams().getFirst("token");
            }
            if(StrUtil.isEmpty(token)){
                return Mono.just(new AuthorizationDecision(false));
            }
            String realToken = token.replace(SecurityConstants.JWT_TOKEN_PREFIX, "");
            JWSObject jwsObject = JWSObject.parse(realToken);
            String userStr = jwsObject.getPayload().toString();
            LoginUser loginUser= JSONUtil.toBean(userStr,LoginUser.class);
            log.info("认证信息："+loginUser);
            if (ClientIdEnum.ADMIN_CLIENT_ID.equals(loginUser.getClientId()) && !pathMatcher.match("/exam-admin/**", uri.getPath())) {
                return Mono.just(new AuthorizationDecision(false));
            }
            if (ClientIdEnum.PORTAL_CLIENT_ID.equals(loginUser.getClientId()) && pathMatcher.match("/exam-admin/**", uri.getPath())) {
                return Mono.just(new AuthorizationDecision(false));
            }
        } catch ( ParseException e) {
            e.printStackTrace();
            return Mono.just(new AuthorizationDecision(false));
        }
        //非管理端路径直接放行
        if (!pathMatcher.match("/exam-auth/**", uri.getPath())) {
            final long eTime = System.currentTimeMillis();
            log.info("认证耗时："+(eTime-sTime));
            return Mono.just(new AuthorizationDecision(true));
        }

        return Mono.just(new AuthorizationDecision(true));
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
