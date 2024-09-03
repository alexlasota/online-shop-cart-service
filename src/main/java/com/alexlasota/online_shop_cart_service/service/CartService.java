package com.alexlasota.online_shop_cart_service.service;

import com.alexlasota.online_shop_cart_service.exceptions.CartNotFoundException;
import com.alexlasota.online_shop_cart_service.mapper.CartMapper;
import com.alexlasota.online_shop_cart_service.model.Cart;
import com.alexlasota.online_shop_cart_service.model.CartItem;
import com.alexlasota.online_shop_cart_service.model.dto.CartDTO;
import com.alexlasota.online_shop_cart_service.model.dto.CartItemDTO;
import com.alexlasota.online_shop_cart_service.repository.CartItemRepository;
import com.alexlasota.online_shop_cart_service.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public CartDTO createCart() {
        Cart cart = new Cart();
        cart = cartRepository.save(cart);
        return cartMapper.toDTO(cart);
    }

    public CartDTO getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + id));
        return cartMapper.toDTO(cart);
    }

    @Transactional
    public CartDTO addItemToCart(Long cartId, CartItemDTO itemDTO) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

        CartItem item = cartMapper.toEntity(itemDTO);
        item.setCart(cart);

        if (itemDTO.getPrice() == null) {
            throw new IllegalArgumentException("Price must be provided for the cart item");
        }
        item.setPrice(itemDTO.getPrice());

        cart.getItems().add(item);
        recalculateTotalPrice(cart);

        cartItemRepository.save(item);
        cart = cartRepository.save(cart);

        return cartMapper.toDTO(cart);
    }

    @Transactional
    public CartDTO removeItemFromCart(Long cartId, Long itemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        recalculateTotalPrice(cart);

        cart = cartRepository.save(cart);

        return cartMapper.toDTO(cart);
    }

    private void recalculateTotalPrice(Cart cart) {
        BigDecimal totalPrice = cart.getItems().stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(totalPrice);
    }
}