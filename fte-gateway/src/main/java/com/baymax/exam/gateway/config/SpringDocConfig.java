package com.baymax.exam.gateway.config;

import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/7 16:59
 * @description：
 * @modified By：
 * @version:
 */
@Configuration
public class SpringDocConfig {
    protected final SwaggerUiConfigProperties swaggerUiConfigProperties;
    protected final RouteDefinitionLocator routeDefinitionLocator;

    public SpringDocConfig(SwaggerUiConfigProperties swaggerUiConfigProperties, RouteDefinitionLocator routeDefinitionLocator) {
        this.swaggerUiConfigProperties = swaggerUiConfigProperties;
        this.routeDefinitionLocator = routeDefinitionLocator;
    }

    @PostConstruct
    public void autoInitSwaggerUrls() {
        List<RouteDefinition> definitions = routeDefinitionLocator.getRouteDefinitions().collectList().block();

        definitions.stream().forEach(routeDefinition -> {
            String name = routeDefinition.getId().replace("ReactiveCompositeDiscoveryClient_", "").toLowerCase();
            AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl(name, routeDefinition.getUri().toString().replace("lb://", "").toLowerCase() + "/v3/api-docs", name);
            Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = swaggerUiConfigProperties.getUrls();
            if (urls == null) {
                urls = new LinkedHashSet<>();
                swaggerUiConfigProperties.setUrls(urls);
            }
            GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build();
            urls.add(swaggerUrl);
        });
    }
}

