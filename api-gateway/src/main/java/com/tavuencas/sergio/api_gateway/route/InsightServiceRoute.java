package com.tavuencas.sergio.api_gateway.route;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class InsightServiceRoute {
    @Bean
    public RouterFunction<ServerResponse> insightRoute() {
        return GatewayRouterFunctions.route("insight-service")
                .route(RequestPredicates.path("/api/v1/insight/**"), http())
                .before(uri("http://localhost:8085"))
                .build();
    }
}
