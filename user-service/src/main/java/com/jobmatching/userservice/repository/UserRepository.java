//package com.jobmatching.userservice.repository;
//
//import com.jobmatching.userservice.model.User;
//import com.jobmatching.userservice.model.enums.Role;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.Optional;
//
//public interface UserRepository extends JpaRepository<User, Long> {
//
//    Optional<User> findByEmail(String email);
//
//    @Query("SELECT u FROM User u " +
//            "LEFT JOIN FETCH u.userProfile " +
//            "LEFT JOIN FETCH u.userJobPreferences " +
//            "WHERE u.email = :email")
//    Optional<User> findByEmailWithDetails(@Param("email") String email);
//
//    Page<User> findAllByRole(Role role, Pageable pageable);
//}


package com.jobmatching.userservice.repository;

import com.jobmatching.userservice.model.User;
import com.jobmatching.userservice.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.userProfile " +
            "LEFT JOIN FETCH u.userJobPreferences " +
            "WHERE u.email = :email")
    Optional<User> findByEmailWithDetails(@Param("email") String email);

    Page<User> findAllByRole(Role role, Pageable pageable);
}
