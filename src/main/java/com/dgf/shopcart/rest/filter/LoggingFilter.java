package com.dgf.shopcart.rest.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * At the moment there is no way to print headers via spring property
 */
@Slf4j
@ControllerAdvice()
@ConditionalOnProperty(name="com.dgf.shopCart.log.response.headers", havingValue="true")
public class LoggingFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        long startTime = System.currentTimeMillis();
        String path = exchange.getRequest().getURI().getPath();
        log.info("Serving '{}'", path);

        return chain.filter(exchange).doAfterTerminate(() -> {
                    exchange.getResponse().getHeaders().forEach((key, value) -> log.info("Response header '{}': {}", key, value));
                    log.info("Served '{}' as {} in {} msec",
                            path,
                            exchange.getResponse().getStatusCode(),
                            System.currentTimeMillis() - startTime);
                }
        );
    }

}