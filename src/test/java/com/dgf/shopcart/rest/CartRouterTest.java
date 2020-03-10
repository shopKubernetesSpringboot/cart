package com.dgf.shopcart.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class CartRouterTest {

    private WebTestClient testClient;


    @BeforeEach
    public void setUp() {

        RouterFunction<?> route = route(GET("/cart/add"), request ->
                ServerResponse.ok().bodyValue("It works!"));
        this.testClient = WebTestClient.bindToRouterFunction(route).build();
    }

    @Test
    public void add() {
        this.testClient.get().uri("/cart/add")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("It works!");
    }

}
