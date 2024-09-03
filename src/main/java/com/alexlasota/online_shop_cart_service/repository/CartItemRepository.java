package com.alexlasota.online_shop_cart_service.repository;

import com.alexlasota.online_shop_cart_service.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
