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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpMethod.GET;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarIntegrationTest {

    public static final String HTTP_LOCALHOST = "http://localhost:";
    private Item item = new Item(1L, "Product1");
    private CartItemAddRequest cartItemAddReq = new CartItemAddRequest(item);

    @LocalServerPort
    private int port;
    private TestRestTemplate restTemplate = new TestRestTemplate(TestRestTemplate.HttpClientOption.ENABLE_COOKIES);

    @Test
    public void addAndList() {
        ResponseEntity<Item> resp = restTemplate.postForEntity(getHost()+"/cart/add", cartItemAddReq, Item.class);
        assertEquals(item,resp.getBody());
        ResponseEntity<Item[]> listResp = restTemplate.exchange(getHost() + "/cart/list", GET, new HttpEntity<>(getCookies(resp)), Item[].class);
        assertArrayEquals(Arrays.array(item),listResp.getBody());
    }

    private String getHost() {
        return HTTP_LOCALHOST +port;
    }

    private HttpHeaders getCookies(ResponseEntity<?> resp) {
        List<String> cookies = resp.getHeaders().get("Set-Cookie");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put(HttpHeaders.COOKIE, cookies);
        return requestHeaders;
    }
}
