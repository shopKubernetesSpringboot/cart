package com.dgf.shopcart.rest.handler;

import com.dgf.shopcart.TestConfig;
import com.dgf.shopcart.model.Item;
import com.dgf.shopcart.rest.handler.req.CartItemAddRequest;
import com.dgf.shopcart.rest.handler.validation.MyValidator;
import com.dgf.shopcart.service.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@Import(TestConfig.class)
@WebFluxTest
public class GlobalExceptionHandlerTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final HandlerStrategies strategies = HandlerStrategies.withDefaults();

    @MockBean
    private CartService service;
    @Autowired
    private MyValidator<CartItemAddRequest> validator;
    @Autowired
    private CartHandler handler;
    private ServerResponse.Context context;

    @BeforeEach
    public void createContext() {
        context = new ServerResponse.Context() {
            @Override
            public List<HttpMessageWriter<?>> messageWriters() {
                return strategies.messageWriters();
            }

            @Override
            public List<ViewResolver> viewResolvers() {
                return strategies.viewResolvers();
            }
        };
    }

    @Test
    public void add() throws JsonProcessingException {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.put("/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(new CartItemAddRequest(null))));
        Mockito.when(service.add(any(), any())).thenReturn(Mono.just(new Item(1L, "product1")));
        ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
        Mono<Void> voidMono = handler.add(serverRequest).flatMap(response -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
            return response.writeTo(exchange, context);
        });
        StepVerifier.create(voidMono)
                .expectError(ServerWebInputException.class).verify();
//        MockServerHttpResponse mockResponse = exchange.getResponse();
//        StepVerifier.create(mockResponse.getBody())
//                .consumeNextWith(System.out::println)
//                .expectComplete().verify();
//        assertThat(mockResponse.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

}