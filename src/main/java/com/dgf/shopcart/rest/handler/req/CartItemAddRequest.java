package com.dgf.shopcart.rest.handler.req;

import com.dgf.shopcart.model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemAddRequest {

    @Valid
    @NotNull
    private Item item;
}
