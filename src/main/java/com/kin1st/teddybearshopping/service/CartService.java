package com.kin1st.teddybearshopping.service;

import com.kin1st.teddybearshopping.model.Cart;
import com.kin1st.teddybearshopping.model.CartItem;
import com.kin1st.teddybearshopping.model.Product;
import com.kin1st.teddybearshopping.model.User;
import com.kin1st.teddybearshopping.repository.CartItemRepository;
import com.kin1st.teddybearshopping.repository.CartRepository;
import com.kin1st.teddybearshopping.repository.ProductRepository;
import com.kin1st.teddybearshopping.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
                       ProductRepository productRepository,
                       CartItemRepository cartItemRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    public Cart getCartByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUser(user);
                    return cartRepository.save(c);
                });
    }

    public Cart addToCart(String username, Long productId, int quantity) {
        Cart cart = getCartByUser(username);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + quantity);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
            cart.getItems().add(item);
        }

        return cartRepository.save(cart);
    }

    public Cart removeItem(String username, Long itemId) {
        Cart cart = getCartByUser(username);
        cart.getItems().removeIf(i -> i.getId().equals(itemId));
        return cartRepository.save(cart);
    }
}