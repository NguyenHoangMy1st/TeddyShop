package com.kin1st.teddybearshopping.service;

import com.kin1st.teddybearshopping.model.*;
import com.kin1st.teddybearshopping.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Order checkoutSelectedItems(String username, List<Long> selectedItemIds,
                                       String name, String email, String address) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> selected = cart.getItems().stream()
                .filter(i -> selectedItemIds.contains(i.getId()))
                .toList();

        if (selected.isEmpty())
            throw new RuntimeException("No items selected for checkout");

        Order order = new Order();
        order.setUser(user);
        order.setCustomerName(name);
        order.setCustomerEmail(email);
        order.setCustomerAddress(address);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        double total = 0;
        for (CartItem ci : selected) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(ci.getProduct());
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getProduct().getPrice());
            order.getItems().add(oi);
            total += ci.getProduct().getPrice() * ci.getQuantity();
        }

        order.setTotalPrice(total);

        // Xóa các item đã thanh toán khỏi giỏ hàng
        cart.getItems().removeAll(selected);
        cartRepository.save(cart);

        return orderRepository.save(order);
    }

    public List<Order> getMyOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(user);
    }
}
