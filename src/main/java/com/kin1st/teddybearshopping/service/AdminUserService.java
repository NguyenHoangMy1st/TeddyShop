package com.kin1st.teddybearshopping.service;

import com.kin1st.teddybearshopping.model.User;
import com.kin1st.teddybearshopping.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminUserService {

    private final UserRepository userRepository;

    public AdminUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Lấy danh sách tất cả user
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<User> searchUsers(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            return userRepository.findAll(pageable);
        }
        return userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFullNameContainingIgnoreCase(
                keyword, keyword, keyword, pageable
        );
    }

    //  Lấy chi tiết 1 user
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Cập nhật user
    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        if (updatedUser.getRole() != null) {
            user.setRole(updatedUser.getRole());
        }

        return userRepository.save(user);
    }

    // Xóa user
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User không tồn tại");
        }
        userRepository.deleteById(id);
    }
}
