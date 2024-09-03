package com.alexlasota.online_shop_cart_service.controller;

import com.alexlasota.online_shop_cart_service.exceptions.CartNotFoundException;
import com.alexlasota.online_shop_cart_service.model.dto.CartDTO;
import com.alexlasota.online_shop_cart_service.model.dto.CartItemDTO;
import com.alexlasota.online_shop_cart_service.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartDTO> createCart() {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.createCart());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCart(id));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartDTO> addItemToCart(@PathVariable Long cartId, @RequestBody CartItemDTO itemDTO) {
        return ResponseEntity.ok(cartService.addItemToCart(cartId, itemDTO));
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartDTO> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(cartId, itemId));
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<String> handleCartNotFoundException(CartNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}