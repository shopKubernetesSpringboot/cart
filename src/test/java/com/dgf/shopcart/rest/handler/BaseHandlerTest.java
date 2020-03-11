package com.dgf.shopcart.rest.handler;

import com.dgf.shopcart.TestConfig;
import com.dgf.shopcart.model.Item;
import com.dgf.shopcart.rest.handler.req.CartItemAddRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Import(TestConfig.class)
@WebFluxTest
public abstract class BaseHandlerTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final HandlerStrategies strategies = HandlerStrategies.withDefaults();
    private final Item item = new Item(1L, "product1");
    final String cartItemAddRequestJson = mapper.writeValueAsString(new CartItemAddRequest(item));
    final String itemJson = mapper.writeValueAsString(item);

    ServerResponse.Context context;

    public BaseHandlerTest() throws JsonProcessingException {
    }

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

    ServerRequest getDefaultServerReq(MockServerWebExchange exchange) {
        return ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
    }

    void assertExchange(MockServerWebExchange exchange, String expectedBody) {
        MockServerHttpResponse mockResponse = exchange.getResponse();
        StepVerifier.create(mockResponse.getBody())
                .consumeNextWith(System.out::println)
                .expectComplete().verify();
        assertThat(mockResponse.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(mockResponse.getBodyAsString().block()).isEqualTo(expectedBody);
    }


}
