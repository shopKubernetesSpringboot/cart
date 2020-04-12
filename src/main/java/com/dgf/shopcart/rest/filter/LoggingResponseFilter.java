package com.dgf.shopcart.rest.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * At the moment there is no way to print headers via spring properties
 */
@Slf4j
@ControllerAdvice()
@ConditionalOnProperty("com.dgf.shopCart.log.response.headers")
public class LoggingResponseFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("LoggingResponseFilter.filter");
        long startTime = System.currentTimeMillis();
        exchange.getRequest().getHeaders().forEach(
                (key, value) -> log.info("{}Request header '{}': {}", exchange.getLogPrefix(), key, value)
        );
        exchange.getRequest().getCookies().forEach(
                (key, value) -> log.info("{}Request cookies '{}': {}", exchange.getLogPrefix(), key, value)
        );
        return chain.filter(exchange).doAfterTerminate(() -> {
                    exchange.getResponse().getHeaders().forEach(
                            (key, value) -> log.info("{}Response header '{}': {}", exchange.getLogPrefix(), key, value)
                    );
                    log.info("{}{} msec", exchange.getLogPrefix(), System.currentTimeMillis() - startTime);
                }
        );
    }

}