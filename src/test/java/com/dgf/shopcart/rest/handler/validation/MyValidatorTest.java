package com.dgf.shopcart.rest.handler.validation;

import com.dgf.shopcart.TestConfig;
import com.dgf.shopcart.model.Item;
import com.dgf.shopcart.rest.handler.req.CartItemAddRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.web.server.ServerWebInputException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Import(TestConfig.class)
@SpringBootTest
public class MyValidatorTest {

    @Autowired
    private MyValidator<CartItemAddRequest> validator;

    @Test()
    public void validationException() {
        assertThrows( ServerWebInputException.class, () -> validator.validate(new CartItemAddRequest(new Item())));
    }
}
