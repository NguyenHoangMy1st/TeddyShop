package com.kin1st.teddybearshopping.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String role;
    @Email(message = "Email không hợp lệ")
    private String email;

    @Size(max = 100, message = "Họ và tên không được quá 100 ký tự")
    private String fullName;

    // Bạn có thể thêm validation cho SĐT nếu cần
    private String phoneNumber;
}
