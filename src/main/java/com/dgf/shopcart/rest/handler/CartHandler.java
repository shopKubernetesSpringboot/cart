package com.dgf.shopcart.rest.handler;

import com.dgf.shopcart.model.Cart;
import com.dgf.shopcart.rest.handler.req.CartItemAddRequest;
import com.dgf.shopcart.rest.handler.validation.MyValidator;
import com.dgf.shopcart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartHandler {

    private static final Mono<ServerResponse> BAD_REQUEST = ServerResponse.badRequest().build();
    public static final String CART = "cart";

    private final CartService service;
    private final MyValidator<CartItemAddRequest> validator;

    public Mono<ServerResponse> add(ServerRequest request) {
            return request.bodyToMono(CartItemAddRequest.class)
                .flatMap(validator::validate)
                .flatMap(item -> service.add(getCart(request), item))
                .flatMap(item -> ok().contentType(APPLICATION_JSON).body(fromValue(item)))
                .switchIfEmpty(BAD_REQUEST);
    }

    public Mono<ServerResponse> list(ServerRequest request) {
        return service.list(getCart(request)).flatMap(resp ->
                ok().contentType(APPLICATION_JSON).body(fromValue(resp))
        );
    }

    private Mono<Cart> getCart(ServerRequest request) {
        return Mono.from(request.session().map(session -> {
            Cart cart = session.getAttribute(CART);
            if (cart == null) {
                cart = new Cart();
                session.getAttributes().put(CART, cart);
            }
            log.info("cart={}", cart);
            return cart;
        }));
    }

}