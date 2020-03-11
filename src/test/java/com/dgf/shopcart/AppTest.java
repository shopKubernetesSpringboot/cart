package com.dgf.shopcart;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    @Test
    public void contextLoads() { //todo doesn't cover
        App.main(new String[]{});
        assertTrue(true);
    }

}
