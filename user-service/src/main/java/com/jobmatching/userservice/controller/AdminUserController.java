
package com.jobmatching.userservice.controller;

import com.jobmatching.userservice.model.User;
import com.jobmatching.userservice.model.enums.Role;
import com.jobmatching.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<User>> list(
            @RequestParam Role role,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userRepository.findAllByRole(role, pageable));
    }


    @PutMapping("/users/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivate(@PathVariable Long id) {
        return userRepository.findById(id).map(u -> {
            u.setIsActive(false);
            userRepository.save(u);
            return ResponseEntity.ok("Deactivated");
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/users/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeRole(@PathVariable Long id, @RequestParam Role newRole) {
        return userRepository.findById(id).map(u -> {
            u.setRole(newRole);
            userRepository.save(u);
            return ResponseEntity.ok("Role updated to " + newRole);
        }).orElse(ResponseEntity.notFound().build());
    }
}
