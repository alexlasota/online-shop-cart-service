package com.alexlasota.online_shop_cart_service.controller;

import com.alexlasota.online_shop_cart_service.exceptions.CartNotFoundException;
import com.alexlasota.online_shop_cart_service.model.dto.CartDTO;
import com.alexlasota.online_shop_cart_service.model.dto.CartItemDTO;
import com.alexlasota.online_shop_cart_service.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    @Test
    void createCart_ReturnsCreatedCart() throws Exception {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(1L);
        when(cartService.createCart()).thenReturn(cartDTO);

        mockMvc.perform(post("/api/v1/carts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getCart_ExistingCart_ReturnsCart() throws Exception {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(1L);
        when(cartService.getCart(1L)).thenReturn(cartDTO);

        mockMvc.perform(get("/api/v1/carts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getCart_NonExistingCart_ReturnsNotFound() throws Exception {
        when(cartService.getCart(1L)).thenThrow(new CartNotFoundException("Cart not found"));

        mockMvc.perform(get("/api/v1/carts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cart not found"));
    }

    @Test
    void addItemToCart_ValidItem_ReturnsUpdatedCart() throws Exception {
        CartItemDTO itemDTO = new CartItemDTO();
        itemDTO.setProductId(1L);
        itemDTO.setQuantity(2);
        itemDTO.setPrice(new BigDecimal("10.00"));

        CartDTO updatedCartDTO = new CartDTO();
        updatedCartDTO.setId(1L);
        when(cartService.addItemToCart(eq(1L), any(CartItemDTO.class))).thenReturn(updatedCartDTO);

        mockMvc.perform(post("/api/v1/carts/{cartId}/items", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void removeItemFromCart_ExistingItem_ReturnsUpdatedCart() throws Exception {
        CartDTO updatedCartDTO = new CartDTO();
        updatedCartDTO.setId(1L);
        when(cartService.removeItemFromCart(1L, 2L)).thenReturn(updatedCartDTO);

        mockMvc.perform(delete("/api/v1/carts/{cartId}/items/{itemId}", 1L, 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}