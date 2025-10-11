package com.jobmatching.userservice.controller;

import com.jobmatching.userservice.dto.*;
import com.jobmatching.userservice.model.User;
import com.jobmatching.userservice.service.UserService;
import com.jobmatching.userservice.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Đăng ký tài khoản
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegistrationDto dto) {
        User user = userService.register(dto);
        return ResponseEntity.ok(UserResponse.fromEntity(user));
    }


    // Lấy thông tin profile (CANDIDATE / EMPLOYER)
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'EMPLOYER')")
    public ResponseEntity<UserResponse> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.getMyProfile(userDetails.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cập nhật thông tin profile (CANDIDATE / EMPLOYER)
    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'EMPLOYER')")
    public ResponseEntity<UserResponse> updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @RequestBody UserUpdateDto dto) {
        return userService.updateProfile(userDetails.getId(), dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
