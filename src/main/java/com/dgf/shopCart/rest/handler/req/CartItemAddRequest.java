package com.dgf.shopCart.rest.handler.req;

import com.dgf.shopCart.model.Item;
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
