package com.kin1st.teddybearshopping.service;

import com.kin1st.teddybearshopping.model.*;
import com.kin1st.teddybearshopping.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         OrderRepository orderRepository,
                         ProductRepository productRepository,
                         UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Review addReview(String username, Long productId, int rating, String comment) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // ✅ Kiểm tra xem user có đơn hàng SHIPPED chứa product này không
        boolean hasBought = orderRepository.existsByUserAndStatusAndItems_Product(
                user, OrderStatus.SHIPPED, product
        );
        if (!hasBought) {
            throw new RuntimeException("Bạn chưa mua sản phẩm này hoặc đơn hàng chưa được giao");
        }

        // ✅ Kiểm tra trùng review
        if (reviewRepository.findByUserAndProduct(user, product).isPresent()) {
            throw new RuntimeException("Bạn đã đánh giá sản phẩm này rồi");
        }

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return reviewRepository.findByProduct(product);
    }
    // ✅ Update review (chỉ user sở hữu review đó)
    public Review updateReview(Long reviewId, String username, int rating, String comment) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy review"));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền sửa review này");
        }

        review.setRating(rating);
        review.setComment(comment);
        return reviewRepository.save(review);
    }

    // ✅ Xóa review (chỉ user sở hữu review đó)
    public void deleteReview(Long reviewId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy review"));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền xóa review này");
        }

        reviewRepository.delete(review);
    }
}
