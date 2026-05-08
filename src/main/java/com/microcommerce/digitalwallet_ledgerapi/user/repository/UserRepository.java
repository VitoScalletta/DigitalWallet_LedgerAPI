package com.microcommerce.digitalwallet_ledgerapi.user.repository;

import com.microcommerce.digitalwallet_ledgerapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
