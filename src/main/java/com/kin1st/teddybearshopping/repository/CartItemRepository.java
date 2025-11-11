package com.kin1st.teddybearshopping.repository;

import com.kin1st.teddybearshopping.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}