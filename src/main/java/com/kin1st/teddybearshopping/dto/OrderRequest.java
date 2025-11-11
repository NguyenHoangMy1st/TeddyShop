package com.kin1st.teddybearshopping.dto;

import com.kin1st.teddybearshopping.validation.PhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    @NotBlank(message = "Tên khách hàng không được để trống")
    @Size(max = 100, message = "Tên khách hàng không quá 100 ký tự")
    private String customerName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String customerEmail;

    @NotBlank(message = "Số điện thoại không được để trống")
    @PhoneNumber
    private String customerPhone;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 255, message = "Địa chỉ không quá 255 ký tự")
    private String customerAddress;

    @NotNull(message = "Danh sách sản phẩm không được để trống")
    private List<@Valid OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        @NotNull(message = "ID sản phẩm không được để trống")
        private Long productId;

        @NotNull(message = "Số lượng không được để trống")
        private Integer quantity;
    }
}