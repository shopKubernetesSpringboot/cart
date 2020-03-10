package com.dgf.shopcart.service;

import com.dgf.shopcart.model.Cart;
import com.dgf.shopcart.model.Item;
import com.dgf.shopcart.rest.handler.req.CartItemAddRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class CartService {

    public Mono<Item> add(Mono<Cart> cart, CartItemAddRequest req) {
        return cart.map(c -> {
            c.getItems().add(req.getItem());
            return req.getItem();
        }).or(Mono.empty());
    }

    public Mono<List<Item>> list(Mono<Cart> cart) {
        return cart.map(Cart::getItems);
    }

}
