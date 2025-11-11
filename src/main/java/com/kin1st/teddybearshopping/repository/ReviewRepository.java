package com.kin1st.teddybearshopping.repository;

import com.kin1st.teddybearshopping.model.Review;
import com.kin1st.teddybearshopping.model.User;
import com.kin1st.teddybearshopping.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct(Product product);
    Optional<Review> findByUserAndProduct(User user, Product product);
}
