package com.dgf.shopCart.service;

import com.dgf.shopCart.model.Cart;
import com.dgf.shopCart.model.Item;
import com.dgf.shopCart.rest.handler.req.CartItemAddRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class CartServiceTest {

    @Autowired
    private CartService service;

    @Test
    public void test() {
        StepVerifier
            .create(service.add(Mono.just(new Cart()),new CartItemAddRequest(new Item(1L,"product1"))))
            .assertNext(loaded -> {
                assertEquals("product1",loaded.getName());
                assertEquals((Long)1L,loaded.getId());
            })
            .expectComplete()
            .verify();
    }
}
