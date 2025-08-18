package com.jobmatching.userservice.service;

import com.jobmatching.userservice.dto.JwtResponseDto;
import com.jobmatching.userservice.dto.LoginRequestDto;
import com.jobmatching.userservice.dto.UserRegistrationDto;
import com.jobmatching.userservice.model.User;
import com.jobmatching.userservice.model.UserProfile;
import com.jobmatching.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public User registerNewUser(UserRegistrationDto registrationDto) {
        User newUser = new User();
        newUser.setEmail(registrationDto.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));
        newUser.setRole(registrationDto.getRole());
        newUser.setActive(true);

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(registrationDto.getFirstName());
        userProfile.setLastName(registrationDto.getLastName());
        userProfile.setPhone(registrationDto.getPhone());

        newUser.setUserProfile(userProfile);
        userProfile.setUser(newUser);

        return userRepository.save(newUser);
    }

    public JwtResponseDto login(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        var jwtToken = jwtService.generateToken(user);
        return new JwtResponseDto(jwtToken);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // PHƯƠNG THỨC BỊ THIẾU LÀ ĐÂY
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}