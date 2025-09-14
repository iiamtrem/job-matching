
package com.jobmatching.userservice.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String email;
    private String password;
    private String role; // "ADMIN" | "EMPLOYER" | "CANDIDATE"
}
