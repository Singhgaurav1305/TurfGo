package com.turf.management.repository; // <-- FIXED PACKAGE

import com.turf.management.model.User; // <-- FIXED IMPORT
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}