package com.alexlasota.online_shop_cart_service.service;

import com.alexlasota.online_shop_cart_service.exceptions.CartNotFoundException;
import com.alexlasota.online_shop_cart_service.mapper.CartMapper;
import com.alexlasota.online_shop_cart_service.model.Cart;
import com.alexlasota.online_shop_cart_service.model.CartItem;
import com.alexlasota.online_shop_cart_service.model.dto.CartDTO;
import com.alexlasota.online_shop_cart_service.model.dto.CartItemDTO;
import com.alexlasota.online_shop_cart_service.repository.CartItemRepository;
import com.alexlasota.online_shop_cart_service.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private CartItemRepository cartItemRepository;

    @MockBean
    private CartMapper cartMapper;

    @Test
    void createCart_ReturnsNewCart() {
        Cart cart = new Cart();
        cart.setId(1L);
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(1L);

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toDTO(cart)).thenReturn(cartDTO);

        CartDTO result = cartService.createCart();

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void getCart_ExistingCart_ReturnsCart() {
        Cart cart = new Cart();
        cart.setId(1L);
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(1L);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartMapper.toDTO(cart)).thenReturn(cartDTO);

        CartDTO result = cartService.getCart(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getCart_NonExistingCart_ThrowsException() {
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> cartService.getCart(1L));
    }

    @Test
    void addItemToCart_ValidItem_ReturnsUpdatedCart() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(new ArrayList<>());

        CartItem cartItem = new CartItem();
        cartItem.setProductId(1L);
        cartItem.setQuantity(2);
        cartItem.setPrice(new BigDecimal("10.00"));

        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId(1L);
        cartItemDTO.setQuantity(2);
        cartItemDTO.setPrice(new BigDecimal("10.00"));

        CartDTO updatedCartDTO = new CartDTO();
        updatedCartDTO.setId(1L);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartMapper.toEntity(cartItemDTO)).thenReturn(cartItem);
        when(cartRepository.save(cart)).thenReturn(cart);
        when(cartMapper.toDTO(cart)).thenReturn(updatedCartDTO);

        CartDTO result = cartService.addItemToCart(1L, cartItemDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cartItemRepository).save(cartItem);
        verify(cartRepository).save(cart);
    }

    @Test
    void removeItemFromCart_ExistingItem_ReturnsUpdatedCart() {
        Cart cart = new Cart();
        cart.setId(1L);
        CartItem itemToRemove = new CartItem();
        itemToRemove.setId(2L);
        cart.setItems(new ArrayList<>(java.util.List.of(itemToRemove)));

        CartDTO updatedCartDTO = new CartDTO();
        updatedCartDTO.setId(1L);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);
        when(cartMapper.toDTO(cart)).thenReturn(updatedCartDTO);

        CartDTO result = cartService.removeItemFromCart(1L, 2L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository).save(cart);
    }
}