package com.kin1st.teddybearshopping.repository;

import com.kin1st.teddybearshopping.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}