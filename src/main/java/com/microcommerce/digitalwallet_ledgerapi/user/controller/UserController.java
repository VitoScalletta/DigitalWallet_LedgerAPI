package com.microcommerce.digitalwallet_ledgerapi.user.controller;

import com.microcommerce.digitalwallet_ledgerapi.user.dto.CreateUserRequest;
import com.microcommerce.digitalwallet_ledgerapi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@Valid @RequestBody CreateUserRequest request) {
        userService.createUser(request);
    }
}
