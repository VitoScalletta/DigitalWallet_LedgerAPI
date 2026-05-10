package com.microcommerce.digitalwallet_ledgerapi.wallet.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class WithdrawRequest {
    @NotNull
    @Positive
    BigDecimal amount;
}
