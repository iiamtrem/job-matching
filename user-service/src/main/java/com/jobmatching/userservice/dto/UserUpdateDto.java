package com.jobmatching.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
    private String email;
    private String role;
}
