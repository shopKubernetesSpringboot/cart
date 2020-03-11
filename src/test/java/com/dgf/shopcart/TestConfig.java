package com.dgf.shopcart;

import com.dgf.shopcart.rest.handler.GlobalErrorAttributes;
import com.dgf.shopcart.rest.handler.req.CartItemAddRequest;
import com.dgf.shopcart.rest.handler.validation.MyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.Validator;

@TestConfiguration
public class TestConfig {

    @Autowired
    private Validator validator;

    @Bean
    public MyValidator<CartItemAddRequest> validator() {
        return new MyValidator<>(validator);
    }
    @Bean
    public GlobalErrorAttributes globalErrorAttributes() {
        return new GlobalErrorAttributes();
    }

}
