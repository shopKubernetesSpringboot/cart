package com.dgf.shopcart.rest.handler.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MyValidator<T> {

    private final Validator validator;

    public Mono<T> validate(T body) {
        Errors errors = new BeanPropertyBindingResult(body, body.getClass().getName());
        validator.validate(body, errors);
        if (!errors.getAllErrors().isEmpty()) {
            throw new ServerWebInputException(errors.toString());
        }
        return Mono.just(body);
    }

}
