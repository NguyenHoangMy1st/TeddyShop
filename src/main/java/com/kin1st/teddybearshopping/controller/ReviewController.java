package com.kin1st.teddybearshopping.controller;

import com.kin1st.teddybearshopping.common.ApiResponse;
import com.kin1st.teddybearshopping.model.Review;
import com.kin1st.teddybearshopping.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<Review>> addReview(Authentication authentication,
                                                         @Valid @RequestBody ReviewRequest req) {
        String username = authentication.getName();
        log.info("User {} add review for product {}", username, req.getProductId());

        Review review = reviewService.addReview(username, req.getProductId(), req.getRating(), req.getComment());
        return ResponseEntity.status(201)
                .body(new ApiResponse<>(true, "Review created", review, null, 201));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<Review>>> getReviews(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewsByProduct(productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Reviews fetched", reviews, null, 200));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Review>> updateReview(Authentication authentication,
                                                            @PathVariable Long id,
                                                            @Valid @RequestBody ReviewRequest req) {
        String username = authentication.getName();
        log.info("User {} update review {} for product {}", username, id, req.getProductId());

        Review updated = reviewService.updateReview(id, username, req.getRating(), req.getComment());
        return ResponseEntity.ok(new ApiResponse<>(true, "Review updated", updated, null, 200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(Authentication authentication,
                                                          @PathVariable Long id) {
        String username = authentication.getName();
        log.info("User {} delete review {}", username, id);

        reviewService.deleteReview(id, username);
        return ResponseEntity.noContent().build();
    }

    // DTO for review requests
    public static class ReviewRequest {
        private Long productId;
        private Integer rating;
        private String comment;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }
}
