package com.jobmatching.userservice.service;

import com.jobmatching.userservice.dto.UserRegistrationDto;
import com.jobmatching.userservice.model.User;
import com.jobmatching.userservice.model.enums.Role;
import com.jobmatching.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(UserRegistrationDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());

        // encode password
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPasswordHash(encodedPassword);

        // parse role từ String -> Role enum; mặc định CANDIDATE nếu null/invalid
        Role parsedRole = Role.CANDIDATE;
        if (dto.getRole() != null) {
            try {
                parsedRole = Role.valueOf(dto.getRole().trim().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                parsedRole = Role.CANDIDATE;
            }
        }
        user.setRole(parsedRole);

        // active mặc định
        user.setIsActive(true);
        return userRepository.save(user);
    }
}
