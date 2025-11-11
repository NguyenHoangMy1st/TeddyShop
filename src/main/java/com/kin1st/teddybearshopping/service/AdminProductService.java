package com.kin1st.teddybearshopping.service;

import com.kin1st.teddybearshopping.exception.ResourceNotFoundException;
import com.kin1st.teddybearshopping.model.Product;
import com.kin1st.teddybearshopping.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AdminProductService {

    private final ProductRepository productRepository;

    public AdminProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ✅ Lấy tất cả sản phẩm (không phân trang)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product ID " + id + " not found"));
    }
    // ✅ Thêm sản phẩm
    public Product createProduct(Product product) {
        // có thể set mặc định active = true nếu null
        if (product.getActive() == null) {
            product.setActive(true);
        }
        return productRepository.save(product);
    }

    // ✅ Cập nhật sản phẩm
    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id).map(existing -> {
            existing.setName(updatedProduct.getName());
            existing.setPrice(updatedProduct.getPrice());
            existing.setSize(updatedProduct.getSize());
            existing.setColor(updatedProduct.getColor());
            existing.setDescription(updatedProduct.getDescription());
            existing.setImageUrl(updatedProduct.getImageUrl());
            existing.setActive(updatedProduct.getActive());
            return productRepository.save(existing);
        });
    }

    // ✅ Xóa sản phẩm
    public boolean deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) return false;

        productRepository.deleteById(id);
        return true;
    }
}
