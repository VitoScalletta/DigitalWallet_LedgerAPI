package com.microcommerce.digitalwallet_ledgerapi.wallet.repository;

import com.microcommerce.digitalwallet_ledgerapi.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
