package com.alexlasota.online_shop_cart_service.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDTO {
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}