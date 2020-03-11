package com.dgf.shopcart.rest.handler;

import com.dgf.shopcart.TestConfig;
import com.dgf.shopcart.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Import(TestConfig.class)
@WebFluxTest
public class GlobalExceptionHandlerTest {

    @MockBean
    private CartService cartService;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;
    @Autowired
    private GlobalErrorAttributes globalErrorAttributes;

    @Test
    public void badRequest() {
        MockServerHttpRequest httpRequest = MockServerHttpRequest.post("/cart/add")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(httpRequest);
        globalErrorAttributes.storeErrorInformation(new ServerWebInputException("Test error"), exchange);
        RouterFunction<ServerResponse> routerFunction = globalExceptionHandler.getRoutingFunction(globalErrorAttributes);
        RouterFunctions.toWebHandler(routerFunction).handle(exchange).block();
        assertEquals(BAD_REQUEST,exchange.getResponse().getStatusCode());
    }

}