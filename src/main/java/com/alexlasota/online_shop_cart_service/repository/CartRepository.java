package com.alexlasota.online_shop_cart_service.repository;

import com.alexlasota.online_shop_cart_service.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
