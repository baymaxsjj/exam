package com.baymax.exam.gateway.filter;

import com.baymax.exam.gateway.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.baymax.exam.common.core.base.SecurityConstants.REQUEST_HEADER_KEY_CLIENT_REAL_IP;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/17 19:32
 * @description：ip获取过滤器
 * @modified By：
 * @version:
 */
@Slf4j
@Component
public class AccessInFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String ip = IpUtils.getRealIpAddress(exchange.getRequest());
        // 该步骤可选。可以传递给下游服务器，用于业务处理
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header(REQUEST_HEADER_KEY_CLIENT_REAL_IP, ip)
                .build();

        log.info("访问入口过滤器AccessInFilter."+ip);
        return chain.filter(exchange.mutate().request(request).build());
    }


    @Override
    public int getOrder() {
        return -3;
    }
}
