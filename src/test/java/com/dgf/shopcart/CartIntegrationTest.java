package com.dgf.shopcart;

import com.dgf.shopcart.model.Item;
import com.dgf.shopcart.rest.handler.req.CartItemAddRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartIntegrationTest {

    private Item item = new Item(1L, "Product1");
    private Item item2 = new Item(2L, "Product2");

    @LocalServerPort
    private int port;

    private ObjectMapper mapper = new ObjectMapper();
    private WebTestClient rest;

    @BeforeAll
    public void setup() {
        rest = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + this.port)
                .filter(logRequest())
                .build();
    }

    @Test
    public void noCredentials() {
        this.rest
                .get()
                .uri("/cart/list")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void validCredentials() {
        this.rest
                .get()
                .uri("/cart/list")
                .headers(userCredentials())
                .exchange()
                .expectStatus().isOk();
//                .expectBody().json("{\"message\":\"Hello user!\"}");
    }

    @Test
    public void contextLoads() {
        App.main(new String[]{});
        assertTrue(true);
    }

    @Test
    public void addAndList() throws JsonProcessingException {
        List<ResponseCookie> sessionCookies = rest.post()
                .uri("/cart/add")
                .headers(userCredentials())
                .headers(contentType())
                .headers(accept())
                .bodyValue(mapper.writeValueAsString(new CartItemAddRequest(item)))
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(mapper.writeValueAsString(item))
                .returnResult().getResponseCookies().get("SESSION");
        rest.post()
                .uri("/cart/add")
                .cookies(cookies(sessionCookies))
                .headers(userCredentials())
                .headers(contentType())
                .headers(accept())
                .bodyValue(mapper.writeValueAsString(new CartItemAddRequest(item2)))
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(mapper.writeValueAsString(item2));
        rest.get()
                .uri("/cart/list")
                .cookies(cookies(sessionCookies))
                .headers(userCredentials())
                .headers(contentType())
                .headers(accept())
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(mapper.writeValueAsString(Arrays.array(item,item2)));
    }

    private Consumer<MultiValueMap<String, String>> cookies(List<ResponseCookie> sessionCookies) {
        return c-> sessionCookies.forEach(c2 -> c.addAll(c2.getName(),Collections.singletonList(c2.getValue())));
    }

    private Consumer<HttpHeaders> userCredentials() {
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return httpHeaders -> httpHeaders.setBasicAuth("user", "user");
    }
    private Consumer<HttpHeaders> contentType() {
        return httpHeaders -> httpHeaders.setContentType(APPLICATION_JSON);
    }
    private Consumer<HttpHeaders> accept() {
        return httpHeaders -> httpHeaders.setAccept(Collections.singletonList(APPLICATION_JSON));
    }

    private Consumer<HttpHeaders> invalidCredentials() {
        return httpHeaders -> httpHeaders.setBasicAuth("user", "INVALID");
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("{}(intTest*) {} {}", clientRequest.logPrefix(), clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}(intTest*) {}={}", clientRequest.logPrefix(), name, value)));
            return Mono.just(clientRequest);
        });
    }
}
