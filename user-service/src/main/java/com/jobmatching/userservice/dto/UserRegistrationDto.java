//package com.jobmatching.userservice.dto;
//
//import com.jobmatching.userservice.model.enums.Role;
//import lombok.Data;
//
//@Data
//public class UserRegistrationDto {
//    // Thông tin cho bảng 'users'
//    private String email;
//    private String password;
//    private Role role;
//
//    // Thông tin cho bảng 'user_profiles'
//    private String firstName;
//    private String lastName;
//    private String phone;
//}


package com.jobmatching.userservice.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String email;
    private String password;
    private String role; // "EMPLOYER" hoặc "JOB_SEEKER"
}
