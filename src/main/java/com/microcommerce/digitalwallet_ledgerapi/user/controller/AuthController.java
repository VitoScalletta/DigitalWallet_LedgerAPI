package com.microcommerce.digitalwallet_ledgerapi.user.controller;

import com.microcommerce.digitalwallet_ledgerapi.common.security.JwtService;
import com.microcommerce.digitalwallet_ledgerapi.user.entity.User;
import com.microcommerce.digitalwallet_ledgerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public record AuthRequest(String userName, String password) {}
    public record AuthResponse(String token) {}

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.userName(), authRequest.password())
        );

        User user = userRepository.findByUserName(authRequest.userName()).orElseThrow();

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token);
    }
}
