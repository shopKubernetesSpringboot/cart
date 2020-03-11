package com.dgf.shopcart.rest.handler;

import com.dgf.shopcart.model.Item;
import com.dgf.shopcart.rest.handler.req.CartItemAddRequest;
import com.dgf.shopcart.rest.handler.validation.MyValidator;
import com.dgf.shopcart.service.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.mock.web.server.MockWebSession;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class CartHandlerTest extends BaseHandlerTest {

    @MockBean
    private CartService service;
    @Autowired
    private MyValidator<CartItemAddRequest> validator;
    @Autowired
    private CartHandler handler;

    public CartHandlerTest() throws JsonProcessingException {
    }

    @Test
    public void add() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.put("/cart/add")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(cartItemAddRequestJson));
        Mockito.when(service.add(any(), any())).thenReturn(Mono.just(new Item(1L, "product1")));
        Mono<Void> voidMono = handler.add(getDefaultServerReq(exchange)).flatMap(response -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
            return response.writeTo(exchange, context);
        });
        StepVerifier.create(voidMono).expectComplete().verify();
        assertExchange(exchange,itemJson);
    }

    @Test
    public void list() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/cart/list"));
        Mockito.when(service.list(any())).thenReturn(Mono.just(new ArrayList<>()));
        StepVerifier.create(handler.list(getDefaultServerReq(exchange)).flatMap(response -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
            return response.writeTo(exchange, context);
        })).expectComplete().verify();
        assertExchange(exchange,"[]");
    }

    private ServerRequest getDefaultServerReq(MockServerWebExchange exchange) {
        return ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
    }

    private void assertExchange(MockServerWebExchange exchange, String expectedBody) {
        MockServerHttpResponse mockResponse = exchange.getResponse();
        StepVerifier.create(mockResponse.getBody())
                .consumeNextWith(System.out::println)
                .expectComplete().verify();
        assertThat(mockResponse.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(mockResponse.getBodyAsString().block()).isEqualTo(expectedBody);
    }

    @Test
    public void getCart() {
        StepVerifier.create(handler.getCart(Mono.just(new MockWebSession())))
                .expectSubscription()
                .assertNext(c -> assertEquals(0, c.getItems().size()))
                .verifyComplete();
    }

}