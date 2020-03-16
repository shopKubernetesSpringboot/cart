package com.dgf.shopcart;

import com.dgf.shopcart.model.Item;
import com.dgf.shopcart.rest.handler.req.CartItemAddRequest;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.web.client.TestRestTemplate.HttpClientOption.ENABLE_COOKIES;
import static org.springframework.http.HttpHeaders.COOKIE;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CartIntegrationTest {

    public static final String HTTP_LOCALHOST = "http://localhost:";
    private Item item = new Item(1L, "Product1");
    private Item item2 = new Item(2L, "Product2");

    @LocalServerPort
    private int port;
    private TestRestTemplate restTemplate = new TestRestTemplate(ENABLE_COOKIES);

    @Test
    public void addAndList() {
        ResponseEntity<Item> resp = restTemplate.postForEntity(getHost()+"/cart/add", new CartItemAddRequest(item), Item.class);
        assertEquals(item,resp.getBody());
        ResponseEntity<Item> resp2 = restTemplate.exchange(getHost()+"/cart/add", POST, new HttpEntity<>(new CartItemAddRequest(item2),getCookies(resp)), Item.class);
        assertEquals(item2,resp2.getBody());
        ResponseEntity<Item[]> listResp = restTemplate.exchange(getHost() + "/cart/list", GET, new HttpEntity<>(getCookies(resp)), Item[].class);
        assertArrayEquals(Arrays.array(item,item2),listResp.getBody());
    }

    private String getHost() {
        return HTTP_LOCALHOST +port;
    }

    private HttpHeaders getCookies(ResponseEntity<?> resp) {
        List<String> cookies = resp.getHeaders().get(SET_COOKIE);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put(COOKIE, cookies);
        return requestHeaders;
    }
}
