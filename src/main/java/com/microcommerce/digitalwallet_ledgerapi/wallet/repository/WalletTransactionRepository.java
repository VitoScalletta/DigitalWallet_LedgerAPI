package com.microcommerce.digitalwallet_ledgerapi.wallet.repository;

import com.microcommerce.digitalwallet_ledgerapi.wallet.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
}
