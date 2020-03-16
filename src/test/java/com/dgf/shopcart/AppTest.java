package com.dgf.shopcart;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

//todo Web server failed to start. Port 8080 was already in use. When app is running via docker
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {

    @Test
    public void contextLoads() { //todo doesn't cover
        App.main(new String[]{});
        assertTrue(true);
    }

}
