package com.dgf.shopcart.rest.handler;

import com.dgf.shopcart.TestConfig;
import com.dgf.shopcart.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Import(TestConfig.class)
@WebFluxTest
public class GlobalExceptionHandlerTest {

    @MockBean
    private CartService cartService;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    public void test() {

//        RouterFunction<ServerResponse> routingFunction = globalExceptionHandler.getRoutingFunction(new GlobalErrorAttributes());

//        HandlerFunction<ServerResponse> handlerFunction = request -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
//        RouterFunction<ServerResponse> routerFunction = RouterFunctions.route(RequestPredicates.all(), handlerFunction);
        GlobalErrorAttributes globalErrorAttributes = new GlobalErrorAttributes();
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.put("/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(""));
        globalErrorAttributes.storeErrorInformation(new ServerWebInputException(""), exchange);
        RouterFunction<ServerResponse> routerFunction = globalExceptionHandler.getRoutingFunction(globalErrorAttributes);

        HttpHandler result = RouterFunctions.toHttpHandler(routerFunction);
        assertNotNull(result);

//        MockServerHttpRequest httpRequest = MockServerHttpRequest.get("https://localhost").build();
//        MockServerHttpResponse httpResponse = new MockServerHttpResponse();
////        ERROR_ATTRIBUTE
////        httpResponse.
//        result.handle(httpRequest, httpResponse).block();
//        assertEquals(HttpStatus.BAD_REQUEST,httpResponse.getStatusCode());
    }

}