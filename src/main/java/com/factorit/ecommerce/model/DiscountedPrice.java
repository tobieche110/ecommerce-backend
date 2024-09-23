package com.factorit.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiscountedPrice {
    private Discount discount;
    private double total;
}
