package com.kin1st.teddybearshopping.controller;

import com.kin1st.teddybearshopping.common.ApiResponse;
import com.kin1st.teddybearshopping.model.Cart;
import com.kin1st.teddybearshopping.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<Cart>> getMyCart(Authentication authentication) {
        String username = authentication.getName();
        log.info("Get cart for user {}", username);
        Cart cart = cartService.getCartByUser(username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cart fetched", cart, null, 200));
    }

    // Add item to cart (body alternative)
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Cart>> addToCart(Authentication authentication,
                                                       @RequestParam Long productId,
                                                       @RequestParam int quantity) {
        String username = authentication.getName();
        log.info("User {} add product {} x{} to cart", username, productId, quantity);
        Cart cart = cartService.addToCart(username, productId, quantity);
        return ResponseEntity.ok(new ApiResponse<>(true, "Item added to cart", cart, null, 200));
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<ApiResponse<Cart>> removeItem(Authentication authentication,
                                                        @PathVariable Long itemId) {
        String username = authentication.getName();
        log.info("User {} remove cart item {}", username, itemId);
        Cart cart = cartService.removeItem(username, itemId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Item removed", cart, null, 200));
    }
}
