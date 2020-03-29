package com.dgf.shopcart;

import com.dgf.shopcart.model.Item;
import com.dgf.shopcart.rest.handler.req.CartItemAddRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AcceptanceTest {

    private Item item = new Item("1", "Product1", 1);
    private Item item2 = new Item("2", "Product2", 1);

    @Autowired
    ApplicationContext context;

    private ObjectMapper mapper = new ObjectMapper();
    private WebTestClient rest;

    @BeforeAll
    public void setup() {
        rest = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void noCredentials() {
        this.rest.get()
                .uri("/cart/list")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void invalidCredentials() {
        this.rest.get()
                .uri("/cart/list")
                .headers(userInvalidCredentials())
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
    }

    @Test
    public void addAndList() throws JsonProcessingException {
        List<ResponseCookie> sessionCookies = rest.get()
                .uri("/cart/list")
                .headers(userCredentials())
                .headers(contentType())
                .headers(accept())
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(mapper.writeValueAsString(Arrays.array()))
                .returnResult().getResponseCookies().get("SESSION");

        rest.mutateWith(csrf()).post()
                .uri("/cart/add")
                .cookies(cookies(sessionCookies))
                .headers(userCredentials())
                .headers(contentType())
                .headers(accept())
                .bodyValue(mapper.writeValueAsString(new CartItemAddRequest(item)))
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(mapper.writeValueAsString(item));
        rest.mutateWith(csrf()).post()
                .uri("/cart/add")
                .cookies(cookies(sessionCookies))
                .headers(userCredentials())
                .headers(contentType())
                .headers(accept())
                .bodyValue(mapper.writeValueAsString(new CartItemAddRequest(item2)))
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(mapper.writeValueAsString(item2));
        rest.mutateWith(csrf()).get()
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

    private Consumer<HttpHeaders> contentType() {
        return httpHeaders -> httpHeaders.setContentType(APPLICATION_JSON);
    }
    private Consumer<HttpHeaders> accept() {
        return httpHeaders -> httpHeaders.setAccept(Collections.singletonList(APPLICATION_JSON));
    }

    private Consumer<HttpHeaders> userCredentials() {
        return httpHeaders -> httpHeaders.setBasicAuth("user", "user");
    }
    private Consumer<HttpHeaders> userInvalidCredentials() {
        return httpHeaders -> httpHeaders.setBasicAuth("user", "INVALID");
    }

}
