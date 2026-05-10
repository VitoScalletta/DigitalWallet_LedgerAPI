package com.microcommerce.digitalwallet_ledgerapi.wallet.dto.response;

import java.math.BigDecimal;

public record WalletResponse(
        Long id,
        String currency,
        BigDecimal balance
) {
}
