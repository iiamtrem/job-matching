package com.jobmatching.userservice.controller;

import com.jobmatching.userservice.dto.UserRegistrationDto;
import com.jobmatching.userservice.model.User;
import com.jobmatching.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto dto) {
        User user = userService.register(dto);
        return ResponseEntity.ok("Đăng ký thành công cho " + user.getEmail() + " với role " + user.getRole());
    }

    @PreAuthorize("hasAnyRole('EMPLOYER', 'CANDIDATE')")
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        return ResponseEntity.ok("Đây là API profile");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/hello")
    public ResponseEntity<?> adminHello() {
        return ResponseEntity.ok("Chào admin!");
    }
}
