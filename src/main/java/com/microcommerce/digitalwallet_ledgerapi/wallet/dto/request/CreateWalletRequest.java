package com.microcommerce.digitalwallet_ledgerapi.wallet.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateWalletRequest(
        @NotNull(message = "Kullanıcı Id'si boş bırakılamaz")
        Long userId,
        @NotBlank(message = "Para birimi boş bırakılamaz")
        String currency,
        @NotNull(message = "Bakiye boş olamaz")
        @DecimalMin("0.0")
        BigDecimal balance
) {

}
