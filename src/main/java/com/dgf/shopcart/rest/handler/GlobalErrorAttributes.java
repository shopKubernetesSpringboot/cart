package com.dgf.shopcart.rest.handler;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;

/**
 * To add messages to response error, override getErrorAttributes & call super.getErrorAttributes
 */
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    public GlobalErrorAttributes() {
        super(true);
    }

}