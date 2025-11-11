package com.kin1st.teddybearshopping.repository;

import com.kin1st.teddybearshopping.model.Cart;
import com.kin1st.teddybearshopping.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}