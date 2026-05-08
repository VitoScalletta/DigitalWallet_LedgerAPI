package com.microcommerce.digitalwallet_ledgerapi.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank(message = "Kullanıcı adı boş bırakılamaz")
        String userName,
        @NotBlank(message = "Email Boş bırakılamaz")
        @Email(message = "Geçerli bir email adresi giriniz")
        String email,
        @NotBlank(message = "Şifre boş bırakılamaz")
        @Size(min = 6, message = "Şifre en az 6 karakter içermeli")
        String password
) {
}
