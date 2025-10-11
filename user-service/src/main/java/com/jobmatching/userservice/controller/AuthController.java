package com.jobmatching.userservice.controller;

import com.jobmatching.userservice.dto.LoginRequest;
import com.jobmatching.userservice.dto.JwtResponse;
import com.jobmatching.userservice.model.User;
import com.jobmatching.userservice.repository.UserRepository;
import com.jobmatching.userservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        // Xác thực thông tin đăng nhập
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Lấy user từ DB
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tạo JWT
        String token = jwtService.generateToken(user);

        // Trả response chứa token
        return ResponseEntity.ok(new JwtResponse(token, "Bearer"));

    }
}
