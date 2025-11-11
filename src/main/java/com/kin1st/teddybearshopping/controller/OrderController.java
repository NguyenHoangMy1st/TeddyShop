package com.kin1st.teddybearshopping.controller;

import com.kin1st.teddybearshopping.common.ApiResponse;
import com.kin1st.teddybearshopping.dto.CheckoutRequest;
import com.kin1st.teddybearshopping.model.Order;
import com.kin1st.teddybearshopping.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    // Checkout selected items from user's cart (selectedItemIds may be null => checkout all)
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<Order>> checkout(Authentication authentication,
                                                       @Valid @RequestBody CheckoutRequest req) {
        String username = authentication.getName();
        log.info("User {} checkout items {}", username, req.getSelectedItemIds());

        Order order = orderService.checkoutSelectedItems(
                username,
                req.getSelectedItemIds(),
                req.getCustomerName(),
                req.getCustomerEmail(),
                req.getCustomerAddress()
        );

        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Order created", order, null, 201));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<Order>>> getMyOrders(Authentication authentication) {
        String username = authentication.getName();
        log.info("Get orders for user {}", username);
        List<Order> orders = orderService.getMyOrders(username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Orders fetched", orders, null, 200));
    }
}
