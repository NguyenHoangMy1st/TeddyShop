package com.kin1st.teddybearshopping.repository;

import com.kin1st.teddybearshopping.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Tìm kiếm theo tên có phân trang
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Lọc theo danh mục có phân trang
//    Page<Product> findByCategoryIgnoreCase(String category, Pageable pageable);

    // Lọc theo khoảng giá có phân trang
    Page<Product> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);
}
