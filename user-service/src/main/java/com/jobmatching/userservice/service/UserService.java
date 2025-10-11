package com.jobmatching.userservice.service;

import com.jobmatching.userservice.dto.UserRegistrationDto;
import com.jobmatching.userservice.dto.UserResponse;
import com.jobmatching.userservice.dto.UserUpdateDto;
import com.jobmatching.userservice.model.User;
import com.jobmatching.userservice.model.enums.Role;
import com.jobmatching.userservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Đăng ký tài khoản
    public User register(UserRegistrationDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.valueOf(dto.getRole().toUpperCase()));
        user.setIsActive(true);
        return userRepository.save(user);
    }

    // Parse Role từ String
    private Role parseRole(String roleStr) {
        if (roleStr == null) return Role.CANDIDATE;
        try {
            return Role.valueOf(roleStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return Role.CANDIDATE;
        }
    }

    // Tìm theo email (dùng khi login)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Lấy profile cá nhân
    public Optional<UserResponse> getMyProfile(Long userId) {
        return userRepository.findById(userId)
                .map(UserResponse::fromEntity);
    }

    // Cập nhật profile cá nhân
    public Optional<UserResponse> updateProfile(Long userId, UserUpdateDto dto) {
        return userRepository.findById(userId).map(u -> {
            if (dto.getEmail() != null) {
                u.setEmail(dto.getEmail());
            }

            if (dto.getRole() != null) {
                try {
                    Role role = Role.valueOf(dto.getRole().trim().toUpperCase());
                    u.setRole(role);
                } catch (IllegalArgumentException ignored) {
                    // Có thể log warning nếu role không hợp lệ
                }
            }

            return userRepository.save(u);
        }).map(UserResponse::fromEntity);
    }


    // Vô hiệu hóa người dùng
    public boolean deactivateUser(Long id) {
        return userRepository.findById(id).map(u -> {
            u.setIsActive(false);
            userRepository.save(u);
            return true;
        }).orElse(false);
    }

    // Admin cập nhật vai trò người dùng
    public boolean updateRole(Long userId, Role newRole) {
        return userRepository.findById(userId).map(u -> {
            u.setRole(newRole);
            userRepository.save(u);
            return true;
        }).orElse(false);
    }

    // Test hash mật khẩu
    @PostConstruct
    public void generateHashForTest() {
        String rawPassword = "admin123";
        String hash = passwordEncoder.encode(rawPassword);
        System.out.println("New hash for 'admin123': " + hash);
    }
}
