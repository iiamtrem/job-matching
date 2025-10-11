package com.jobmatching.userservice.dto;

import com.jobmatching.userservice.model.User;
import com.jobmatching.userservice.model.enums.Role;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private Role role;

    public static UserResponse fromEntity(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}
