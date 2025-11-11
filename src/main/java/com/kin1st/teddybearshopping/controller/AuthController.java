package com.kin1st.teddybearshopping.controller;

import com.kin1st.teddybearshopping.dto.AuthResponse;
import com.kin1st.teddybearshopping.dto.LoginRequest;
import com.kin1st.teddybearshopping.dto.SignupRequest;
import com.kin1st.teddybearshopping.model.User;
import com.kin1st.teddybearshopping.repository.UserRepository;
import com.kin1st.teddybearshopping.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Đăng ký tài khoản mới
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request, BindingResult bindingResult) {
        // Kiểm tra lỗi validation
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            return ResponseEntity.badRequest().body(errors);
        }

        // Kiểm tra username đã tồn tại chưa
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("username", "{user.username.exists}"));
        }

        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("email", "{user.email.exists}"));
        }

        // Tạo user mới
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole("ROLE_USER");
        user.setEnabled(true);

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return ResponseEntity.ok(new AuthResponse("Đăng ký thành công", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request,
            BindingResult bindingResult
    ) {
        // [GIỮ NGUYÊN] Xử lý lỗi validation DTO
        if (bindingResult.hasErrors()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Thông tin không hợp lệ");

            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            response.put("errors", errors);

            return ResponseEntity.badRequest().body(response);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Tìm user sau khi xác thực thành công (Nếu thành công thì user chắc chắn tồn tại)
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Lỗi nội bộ: Không tìm thấy người dùng đã được xác thực."));

            // Tạo token JWT
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("email", user.getEmail());
            userInfo.put("fullName", user.getFullName());
            userInfo.put("phoneNumber", user.getPhoneNumber());
            userInfo.put("role", user.getRole());
            userInfo.put("enabled", user.isEnabled());

            Map<String, Object> response = Map.of(
                    "success", true,
                    "message", "Đăng nhập thành công",
                    "token", token,
                    "user", userInfo
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            String message = (e.getMessage() != null && e.getMessage().contains("disabled"))
                    ? "Tài khoản đã bị vô hiệu hóa"
                    : "Tên đăng nhập hoặc mật khẩu không chính xác";

            Map<String, Object> response = Map.of("success", false, "message", message);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (Exception e) {
            Map<String, Object> response = Map.of(
                    "success", false,
                    "message", "Đã xảy ra lỗi trong quá trình đăng nhập",
                    "error", e.getMessage()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}