package com.dgf.shopCart.service;

import com.dgf.shopCart.model.Cart;
import com.dgf.shopCart.model.Item;
import com.dgf.shopCart.rest.handler.req.CartItemAddRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
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

    public void initData() {
//        log.warn("Auto generating data for films & customers.");
//        filmService.createFilms(FILMS.get()).map(Film::getId)
//            .collectList().map(films ->
//                customerService.createCustomers(CUSTOMERS.get()).map(Customer::getId)
//                    .map(customer -> new UserFilmsRequest(customer, films))
//            ).subscribe(map -> map.subscribe(map2->log.warn("initData generated request:\n" + toJson(map2))));
    }

}
