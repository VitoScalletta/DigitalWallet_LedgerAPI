package com.microcommerce.digitalwallet_ledgerapi.wallet.service;

import com.microcommerce.digitalwallet_ledgerapi.user.entity.User;
import com.microcommerce.digitalwallet_ledgerapi.user.repository.UserRepository;
import com.microcommerce.digitalwallet_ledgerapi.wallet.dto.CreateWalletRequest;
import com.microcommerce.digitalwallet_ledgerapi.wallet.dto.DepositRequest;
import com.microcommerce.digitalwallet_ledgerapi.wallet.dto.WalletResponse;
import com.microcommerce.digitalwallet_ledgerapi.wallet.entity.TransactionType;
import com.microcommerce.digitalwallet_ledgerapi.wallet.entity.Wallet;
import com.microcommerce.digitalwallet_ledgerapi.wallet.entity.WalletTransaction;
import com.microcommerce.digitalwallet_ledgerapi.wallet.repository.WalletRepository;
import com.microcommerce.digitalwallet_ledgerapi.wallet.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository transcationRepo;
    public void createWallet(CreateWalletRequest request){
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("user not found"));
        Wallet wallet = new Wallet();
        wallet.setBalance(request.balance());
        wallet.setCurrency(request.currency());
        wallet.setUser(user);
        walletRepository.save(wallet);
    }

    public List<WalletResponse> findAllByUserId(Long userId){
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        return wallets.stream()
                .map(wallet -> new WalletResponse(wallet.getId(),wallet.getCurrency(),wallet.getBalance())).toList();
    }

    @Transactional
    public void deposit(Long walletId, DepositRequest request){
        Wallet  wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("wallet not found"));
        BigDecimal newBalance = wallet.getBalance().add(request.amount());

        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setWallet(wallet);
        walletTransaction.setAmount(request.amount());
        wallet.setBalance(newBalance);
        walletTransaction.setType(TransactionType.DEPOSIT);
        walletTransaction.setReferenceNumber(UUID.randomUUID().toString());

        walletRepository.save(wallet);
        transcationRepo.save(walletTransaction);
    }
}
