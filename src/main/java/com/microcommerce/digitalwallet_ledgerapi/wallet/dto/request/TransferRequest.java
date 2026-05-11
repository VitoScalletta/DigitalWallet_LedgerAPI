package com.microcommerce.digitalwallet_ledgerapi.wallet.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull
        Long toWalletId,
        @NotNull
        @Positive
        BigDecimal amount
) {
}
