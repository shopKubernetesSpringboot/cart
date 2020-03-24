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

    private final Item item = new Item(1L, "product1", 1);

    @Test
    public void add() {
        StepVerifier
            .create(service.add(Mono.just(new Cart()),new CartItemAddRequest(item)))
            .assertNext(loaded -> {
                assertEquals(item.getName(),loaded.getName());
                assertEquals((Long)1L,loaded.getId());
            })
            .expectComplete()
            .verify();
    }

    @Test
    public void addExisting() {
        Cart cart = new Cart();
        cart.getItems().add(item);
        StepVerifier
                .create(service.add(Mono.just(cart),new CartItemAddRequest(item)))
                .assertNext(loaded -> assertEquals(2,loaded.getQuantity()))
                .expectComplete()
                .verify();
    }

    @Test
    public void list() {
        Cart cart = new Cart();
        cart.getItems().add(item);
        StepVerifier
            .create(service.list(Mono.just(cart)))
            .assertNext(loaded -> assertEquals(1,loaded.size()))
            .expectComplete()
            .verify();
    }

    @Test
    public void delete() {
        Cart cart = new Cart();
        cart.getItems().add(item);
        StepVerifier
            .create(service.delete(Mono.just(cart)))
            .assertNext(loaded -> assertEquals(0,loaded.size()))
            .expectComplete()
            .verify();
    }
}
