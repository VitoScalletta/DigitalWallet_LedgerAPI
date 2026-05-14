package com.microcommerce.digitalwallet_ledgerapi.user.service;

import com.microcommerce.digitalwallet_ledgerapi.user.dto.CreateUserRequest;
import com.microcommerce.digitalwallet_ledgerapi.user.entity.User;
import com.microcommerce.digitalwallet_ledgerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public void createUser(CreateUserRequest request) {
        User user = new User();
        user.setUserName(request.userName());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        userRepository.save(user);
    }
}
