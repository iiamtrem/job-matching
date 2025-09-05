////package com.jobmatching.userservice.service;
////
////import com.jobmatching.userservice.dto.UserRegistrationDto;
////import com.jobmatching.userservice.model.User;
////import com.jobmatching.userservice.model.enums.Role;
////import com.jobmatching.userservice.repository.UserRepository;
////import lombok.RequiredArgsConstructor;
////import org.springframework.security.crypto.password.PasswordEncoder;
////import org.springframework.stereotype.Service;
////
////@Service
////@RequiredArgsConstructor
////public class UserService {
////
////    private final UserRepository userRepository;
////    private final PasswordEncoder passwordEncoder;
////
////    public User register(UserRegistrationDto dto) {
////        User user = new User();
////        user.setEmail(dto.getEmail());
////
////        // encode password
////        String encodedPassword = passwordEncoder.encode(dto.getPassword());
////        user.setPasswordHash(encodedPassword);
////
////        // parse role từ String -> Role enum; mặc định CANDIDATE nếu null/invalid
////        Role parsedRole = Role.CANDIDATE;
////        if (dto.getRole() != null) {
////            try {
////                parsedRole = Role.valueOf(dto.getRole().trim().toUpperCase());
////            } catch (IllegalArgumentException ignored) {
////                parsedRole = Role.CANDIDATE;
////            }
////        }
////        user.setRole(parsedRole);
////
////        // active mặc định
////        user.setIsActive(true);
////        return userRepository.save(user);
////    }
////}
//
//
//package com.jobmatching.userservice.service;
//
//import com.jobmatching.userservice.model.User;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Service
//public class JwtService {
//
//    @Value("${application.security.jwt.secret-key}")
//    private String jwtSecretBase64; // phải là Base64
//
//    private SecretKey getSignInKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretBase64);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public String generateToken(User user) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("role", user.getRole().name());
//        claims.put("userId", user.getId());
//
//        return Jwts.builder()
//                .claims(claims)
//                .subject(user.getEmail())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10h
//                .signWith(getSignInKey())
//                .compact();
//    }
//
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        final String username = extractEmail(token);
//        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
//    }
//
//    public String extractEmail(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public String extractRole(String token) {
//        return extractAllClaims(token).get("role", String.class);
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractClaim(token, Claims::getExpiration).before(new Date());
//    }
//
//    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
//        final Claims claims = extractAllClaims(token);
//        return resolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSignInKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}


package com.jobmatching.userservice.service;

import com.jobmatching.userservice.dto.UserRegistrationDto;
import com.jobmatching.userservice.model.User;
import com.jobmatching.userservice.model.enums.Role;
import com.jobmatching.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(UserRegistrationDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());

        String encoded = passwordEncoder.encode(dto.getPassword());
        user.setPasswordHash(encoded);

        Role parsed = Role.CANDIDATE;
        if (dto.getRole() != null) {
            try {
                parsed = Role.valueOf(dto.getRole().trim().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                parsed = Role.CANDIDATE;
            }
        }
        user.setRole(parsed);
        user.setIsActive(true); // nếu field là Boolean; nếu boolean: user.setActive(true);

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean deactivateUser(Long id) {
        return userRepository.findById(id).map(u -> {
            u.setIsActive(false);
            userRepository.save(u);
            return true;
        }).orElse(false);
    }

    public boolean updateRole(Long userId, Role newRole) {
        return userRepository.findById(userId).map(u -> {
            u.setRole(newRole);
            userRepository.save(u);
            return true;
        }).orElse(false);
    }
}
