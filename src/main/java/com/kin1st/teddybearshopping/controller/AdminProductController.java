package com.kin1st.teddybearshopping.controller;

import com.kin1st.teddybearshopping.common.ApiResponse;
import com.kin1st.teddybearshopping.exception.ResourceNotFoundException;
import com.kin1st.teddybearshopping.model.Product;
import com.kin1st.teddybearshopping.service.AdminProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/products")
@Slf4j
public class AdminProductController {

    private final AdminProductService adminProductService;

    public AdminProductController(AdminProductService adminProductService) {
        this.adminProductService = adminProductService;
    }

    // ✅ Lấy tất cả sản phẩm (không phân trang)
    @GetMapping
    public ApiResponse<List<Product>> getAllProducts() {
        return new ApiResponse<>(
                true,
                "Fetched all products successfully",
                adminProductService.getAllProducts(),
                null,
                HttpStatus.OK.value()
        );
    }
    @GetMapping("/{id}")
    public ApiResponse<Product> getProductById(@PathVariable Long id) {
        Product product = adminProductService.getProductById(id);

        return new ApiResponse<>(
                true,
                "Product found",
                product,
                null,
                HttpStatus.OK.value()
        );
    }
    // ✅ Thêm sản phẩm
    @PostMapping
    public ApiResponse<Product> createProduct(@Valid @RequestBody Product product) {
        Product created = adminProductService.createProduct(product);
        return new ApiResponse<>(
                true,
                "Product created successfully",
                created,
                null,
                HttpStatus.CREATED.value()
        );
    }

    // ✅ Cập nhật sản phẩm
    @PutMapping("/{id}")
    public ApiResponse<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        return adminProductService.updateProduct(id, product)
                .map(updated -> new ApiResponse<>(
                        true,
                        "Product updated successfully",
                        updated,
                        null,
                        HttpStatus.OK.value()))
                .orElseThrow(() -> new ResourceNotFoundException("Product ID " + id + " not found"));
    }

    // ✅ Xóa sản phẩm
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = adminProductService.deleteProduct(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Product ID " + id + " not found");
        }

        return new ApiResponse<>(
                true,
                "Product deleted successfully",
                null,
                null,
                HttpStatus.OK.value()
        );
    }
}
