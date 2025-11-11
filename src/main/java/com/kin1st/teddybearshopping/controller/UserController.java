package com.kin1st.teddybearshopping.controller;

import com.kin1st.teddybearshopping.common.ApiResponse;
import com.kin1st.teddybearshopping.dto.ChangePasswordRequest;
import com.kin1st.teddybearshopping.dto.UserResponse;
import com.kin1st.teddybearshopping.exception.BusinessException;
import com.kin1st.teddybearshopping.exception.ResourceNotFoundException;
import com.kin1st.teddybearshopping.model.User;
import com.kin1st.teddybearshopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Get current user info (hide password)
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getMyInfo(Authentication authentication) {
        String username = authentication.getName();
        log.info("Get profile for user={}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(null); // hide
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile fetched", user, null, 200));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<User>> updateMyInfo(Authentication authentication,
                                                          @Valid @RequestBody UserResponse updatedUserDto) { // <--- Đã đổi sang DTO
        String username = authentication.getName();
        log.info("Update profile for user={}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        // Cập nhật từ DTO
        if (updatedUserDto.getEmail() != null) user.setEmail(updatedUserDto.getEmail());
        if (updatedUserDto.getFullName() != null) user.setFullName(updatedUserDto.getFullName());
        if (updatedUserDto.getPhoneNumber() != null) user.setPhoneNumber(updatedUserDto.getPhoneNumber());

        userRepository.save(user);
        user.setPassword(null);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated successfully", user, null, 200));
    }

    // Change password
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(Authentication authentication,
                                                            @Valid @RequestBody ChangePasswordRequest req) {
        String username = authentication.getName();
        log.info("Change password request for user={}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")); // Nên dùng ResourceNotFoundException (đã có handler 404)

        // 1. Lỗi Mật khẩu cũ không đúng -> BusinessException (401 Unauthorized)
        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new BusinessException(
                    "Mật khẩu cũ không đúng",
                    "OLD_PASSWORD_MISMATCH",
                    HttpStatus.UNAUTHORIZED // Báo lỗi 401
            );
        }

        // 2. Lỗi Mật khẩu mới trùng mật khẩu cũ -> BusinessException (400 Bad Request)
        if (passwordEncoder.matches(req.getNewPassword(), user.getPassword())) {
            throw new BusinessException(
                    "Mật khẩu mới phải khác mật khẩu cũ",
                    "PASSWORD_REUSE_FORBIDDEN",
                    HttpStatus.BAD_REQUEST // Báo lỗi 400
            );
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Password changed successfully", null, null, 200));
    }
}
