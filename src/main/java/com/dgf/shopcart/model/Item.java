package com.dgf.shopcart.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @NotNull
    private Long id;
    @NotNull
    private String name;
    private Integer quantity=1;
}
