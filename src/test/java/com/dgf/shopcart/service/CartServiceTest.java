package com.dgf.shopcart.service;

import com.dgf.shopcart.model.Cart;
import com.dgf.shopcart.model.Item;
import com.dgf.shopcart.rest.handler.req.CartItemAddRequest;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartServiceTest {

    private CartService service = new CartService();

    @Test
    public void add() {
        StepVerifier
            .create(service.add(Mono.just(new Cart()),new CartItemAddRequest(new Item(1L,"product1"))))
            .assertNext(loaded -> {
                assertEquals("product1",loaded.getName());
                assertEquals((Long)1L,loaded.getId());
            })
            .expectComplete()
            .verify();
    }
    @Test
    public void list() {
        StepVerifier
            .create(service.list(Mono.just(new Cart())))
            .assertNext(loaded -> assertEquals(0,loaded.size()))
            .expectComplete()
            .verify();
    }
}
