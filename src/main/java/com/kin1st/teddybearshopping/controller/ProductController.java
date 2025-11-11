package com.kin1st.teddybearshopping.controller;

import com.kin1st.teddybearshopping.common.ApiResponse;
import com.kin1st.teddybearshopping.dto.PagedResponse;
import com.kin1st.teddybearshopping.model.Product;
import com.kin1st.teddybearshopping.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Lấy danh sách sản phẩm có phân trang
    @GetMapping
    public ApiResponse<PagedResponse<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Product> productPage = productService.getAllProducts(PageRequest.of(page, size));

        return new ApiResponse<>(
                true,
                "Fetched products successfully",
                new PagedResponse<>(
                        productPage.getContent(),
                        productPage.getNumber(),
                        productPage.getTotalPages(),
                        productPage.getTotalElements()
                ),
                null,
                HttpStatus.OK.value()
        );
    }

    // Xem chi tiết sản phẩm
    @GetMapping("/{id}")
    public ApiResponse<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);

        return new ApiResponse<>(
                true,
                "Product found",
                product,
                null,
                HttpStatus.OK.value()
        );
    }
    // Tìm kiếm sản phẩm theo tên có phân trang
    @GetMapping("/search")
    public ApiResponse<PagedResponse<Product>> searchProducts(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Product> productPage = productService.searchByName(name, PageRequest.of(page, size));
        return new ApiResponse<>(
                true,
                "Search results",
                new PagedResponse<>(
                        productPage.getContent(),
                        productPage.getNumber(),
                        productPage.getTotalPages(),
                        productPage.getTotalElements()
                ),
                null,
                HttpStatus.OK.value()
        );
    }
}
