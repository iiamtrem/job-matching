
package com.jobmatching.userservice.service;

import com.jobmatching.userservice.dto.UserRegistrationDto;
import com.jobmatching.userservice.model.User;
import com.jobmatching.userservice.model.enums.Role;
import com.jobmatching.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(UserRegistrationDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());

        String encoded = passwordEncoder.encode(dto.getPassword());
        user.setPasswordHash(encoded);

        Role parsed = Role.CANDIDATE;
        if (dto.getRole() != null) {
            try {
                parsed = Role.valueOf(dto.getRole().trim().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                parsed = Role.CANDIDATE;
            }
        }
        user.setRole(parsed);
        user.setIsActive(true);

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean deactivateUser(Long id) {
        return userRepository.findById(id).map(u -> {
            u.setIsActive(false);
            userRepository.save(u);
            return true;
        }).orElse(false);
    }

    public boolean updateRole(Long userId, Role newRole) {
        return userRepository.findById(userId).map(u -> {
            u.setRole(newRole);
            userRepository.save(u);
            return true;
        }).orElse(false);
    }
}
