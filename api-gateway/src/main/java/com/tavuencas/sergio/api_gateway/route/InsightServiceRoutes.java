package com.tavuencas.sergio.api_gateway.route;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class InsightServiceRoutes {
    @Bean
    public RouterFunction<ServerResponse> insightRoute() {
        return GatewayRouterFunctions.route("insight-service")
                .route(RequestPredicates.path("/api/v1/insight/**"), http())
                .before(uri("http://localhost:8085"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(
                        "insightServiceCircuitBreaker",
                        URI.create("forward:/insightFallbackRoute")
                ))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> insightFallbackRoute() {
        return GatewayRouterFunctions.route("insightFallbackRoute")
                .route(RequestPredicates.path("/insightFallbackRoute"),
                        request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body("Insight service is down.")
                )
                .build();
    }
}
