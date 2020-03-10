package com.dgf.shopcart.rest;

import com.dgf.shopcart.model.Item;
import com.dgf.shopcart.rest.handler.CartHandler;
import com.dgf.shopcart.rest.handler.req.CartItemAddRequest;
import com.dgf.shopcart.rest.handler.validation.MyValidator;
import com.dgf.shopcart.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@WebFluxTest
//@RunWith(SpringRunner.class)
public class CartHandlerTest {

    @MockBean
    private CartService service;
    @MockBean
    private MyValidator<CartItemAddRequest> validator;
    @Autowired
    private CartHandler handler;
    private ServerResponse.Context context;
//    private WebTestClient testClient;


    @BeforeEach
    public void createContext() {
        HandlerStrategies strategies = HandlerStrategies.withDefaults();
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
    public void handle() {

//        Gson gson = new Gson();

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.put("/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .body("{ \"item\": { \"id\": 1, \"name\": \"product1\" } }"));

        MockServerHttpResponse mockResponse = exchange.getResponse();

        Mockito.when(validator.validate(any())).thenReturn(Mono.just(new CartItemAddRequest(new Item(1L,"product1"))));
        Mockito.when(service.add(any(),any())).thenReturn(Mono.just(new Item(1L,"product1")));

        ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

        Mono<ServerResponse> serverResponseMono = handler.add(serverRequest);

        Mono<Void> voidMono = serverResponseMono.flatMap(response -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
            return response.writeTo(exchange, context);
        });

        StepVerifier.create(voidMono)
                .expectComplete().verify();

        StepVerifier.create(mockResponse.getBody())
                .consumeNextWith(System.out::println)
                .expectComplete().verify();

        assertThat(mockResponse.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);

    }

//    @BeforeEach
//    public void setUp() {
//        CartHandler usersHandler = new CartHandler(service, validator);
//        this.testClient = WebTestClient.bindToController(usersHandler).configureClient().baseUrl("/cart").build();
//    }
//
//    @Test
//    public void add() {
//        this.testClient.put().uri("/add")
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//                .bodyValue("{ 'item': { 'id': 1, 'name': 'product1' } }")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .json("");
//    }

}
