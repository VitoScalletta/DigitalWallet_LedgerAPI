package com.microcommerce.digitalwallet_ledgerapi.user.repository;

import com.microcommerce.digitalwallet_ledgerapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
}
