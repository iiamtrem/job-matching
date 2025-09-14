// src/main/java/com/jobmatching/userservice/dto/LoginRequest.java
package com.jobmatching.userservice.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;

    public void setUsername(String username) { this.email = username; }
}
