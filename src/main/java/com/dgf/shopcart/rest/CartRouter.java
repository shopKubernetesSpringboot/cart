package com.dgf.shopcart.rest;

import com.dgf.shopcart.rest.handler.CartHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CartRouter {

    @Bean
    public RouterFunction<ServerResponse> add(CartHandler handler) {
        return route(PUT("/cart/add").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), handler::add);
    }
    @Bean
    public RouterFunction<ServerResponse> list(CartHandler handler) {
        return route(GET("/cart/list"), handler::list);
    }
}
