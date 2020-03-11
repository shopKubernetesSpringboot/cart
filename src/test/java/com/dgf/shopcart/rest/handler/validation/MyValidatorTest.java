package com.dgf.shopcart.rest.handler.validation;

import com.dgf.shopcart.TestConfig;
import com.dgf.shopcart.model.Item;
import com.dgf.shopcart.rest.handler.req.CartItemAddRequest;
import com.dgf.shopcart.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.web.server.ServerWebInputException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Import(TestConfig.class)
@WebFluxTest
public class MyValidatorTest {

    @MockBean
    private CartService service;
    @Autowired
    private MyValidator<CartItemAddRequest> validator;

    @Test()
    public void validationException() {
        assertThrows( ServerWebInputException.class, () -> validator.validate(new CartItemAddRequest(new Item())));
    }
}
