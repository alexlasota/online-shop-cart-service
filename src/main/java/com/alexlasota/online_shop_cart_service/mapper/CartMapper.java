package com.alexlasota.online_shop_cart_service.mapper;

import com.alexlasota.online_shop_cart_service.model.Cart;
import com.alexlasota.online_shop_cart_service.model.CartItem;
import com.alexlasota.online_shop_cart_service.model.dto.CartDTO;
import com.alexlasota.online_shop_cart_service.model.dto.CartItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartDTO toDTO(Cart cart);

    CartItemDTO toDTO(CartItem cartItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "items", ignore = true)
    Cart toEntity(CartDTO cartDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cart", ignore = true)
    CartItem toEntity(CartItemDTO cartItemDTO);
}