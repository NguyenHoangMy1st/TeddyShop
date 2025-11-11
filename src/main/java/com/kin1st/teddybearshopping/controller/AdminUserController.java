package com.kin1st.teddybearshopping.controller;

import com.kin1st.teddybearshopping.common.ApiResponse;
import com.kin1st.teddybearshopping.model.User;
import com.kin1st.teddybearshopping.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<User>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        String sortBy = sort[0];
        String sortDir = sort.length > 1 ? sort[1] : "asc";
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<User> result = adminUserService.getAllUsers(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Users fetched", result, null, 200));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<User>>> searchUsers(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        String sortBy = sort[0];
        String sortDir = sort.length > 1 ? sort[1] : "asc";
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<User> result = adminUserService.searchUsers(keyword, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Search results", result, null, 200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        User u = adminUserService.getUserById(id);
        u.setPassword(null);
        return ResponseEntity.ok(new ApiResponse<>(true, "User fetched", u, null, 200));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User u = adminUserService.updateUser(id, updatedUser);
        u.setPassword(null);
        return ResponseEntity.ok(new ApiResponse<>(true, "User updated", u, null, 200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        adminUserService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User deleted", "User with ID " + id + " deleted", null, 200));
    }
}
