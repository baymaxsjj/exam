package com.baymax.exam.gateway.filter;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/7 19:50
 * @description：
 * @modified By：
 * @version:
 */

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * swagger v3/api-docs缺失basePath 过滤器
 *
 * @Author 955
 * @Date 2022-09-22 11:31
 * @Description
 */

@Slf4j
@Component
public class SwaggerGlobalFilter implements GlobalFilter, Ordered {

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        String host = request.getLocalAddress().getHostString();
        int port = request.getLocalAddress().getPort();
        if (!path.endsWith("/v3/api-docs")) {
            return chain.filter(exchange);
        }
        String[] pathArray = path.split("/");
        System.out.println(pathArray);
        String basePath = pathArray[1];
        ServerHttpResponse originalResponse = exchange.getResponse();
        // 定义新的消息头
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (super.getStatusCode().equals(HttpStatus.OK) && body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        List<String> list = new ArrayList<>();
                        dataBuffers.forEach(dataBuffer -> {
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            DataBufferUtils.release(dataBuffer);
                            list.add(new String(content, Charset.forName("UTF-8")));
                        });
                        String s = this.listToString(list);
                        JSONObject jsonObject = JSON.parseObject(s);
                        jsonObject.put("host", host + ":" + port);
                        jsonObject.put("basePath", basePath);
                        s = jsonObject.toString();
                        // 设置更新后的header请求头长度
                        int length = s.getBytes().length;
                        HttpHeaders headers = originalResponse.getHeaders();
                        headers.setContentLength(length);
                        return bufferFactory().wrap(s.getBytes(Charset.forName("UTF-8")));
                    }));
                }
                return super.writeWith(body);
            }

            @Override
            public HttpHeaders getHeaders() {
                // 获取父类原始ServerHttpResponse的header请求头信息，这是代理Delegate类型
                HttpHeaders httpHeaders = super.getHeaders();
                httpHeaders.set(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
                return httpHeaders;
            }

            private String listToString(List<String> list) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : list) {
                    stringBuilder.append(s);
                }
                return stringBuilder.toString();
            }
        };

        // replace response with decorator
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return -2;
    }

}
