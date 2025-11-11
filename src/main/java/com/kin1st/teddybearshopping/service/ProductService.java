package com.kin1st.teddybearshopping.service;

import com.kin1st.teddybearshopping.exception.ResourceNotFoundException;
import com.kin1st.teddybearshopping.model.Product;
import com.kin1st.teddybearshopping.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ✅ Lấy tất cả sản phẩm có phân trang
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // ✅ Lấy sản phẩm theo ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product ID " + id + " not found"));
    }

    // ✅ Tìm kiếm theo tên có phân trang
    public Page<Product> searchByName(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    // ✅ Lọc theo danh mục
//    public Page<Product> getByCategory(String category, Pageable pageable) {
//        return productRepository.findByCategoryIgnoreCase(category, pageable);
//    }

    // ✅ Lọc theo giá
    public Page<Product> filterByPrice(Double minPrice, Double maxPrice, Pageable pageable) {
        return productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
    }

    // ✅ Thêm hoặc cập nhật sản phẩm
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // ✅ Xóa sản phẩm
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);    }
}
