package com.dgf.shopcart.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Cart {

    List<Item> items = new ArrayList<>();
}
