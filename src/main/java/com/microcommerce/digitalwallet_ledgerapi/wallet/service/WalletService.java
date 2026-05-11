package com.microcommerce.digitalwallet_ledgerapi.wallet.service;

import com.microcommerce.digitalwallet_ledgerapi.user.entity.User;
import com.microcommerce.digitalwallet_ledgerapi.user.repository.UserRepository;
import com.microcommerce.digitalwallet_ledgerapi.wallet.dto.request.CreateWalletRequest;
import com.microcommerce.digitalwallet_ledgerapi.wallet.dto.request.DepositRequest;
import com.microcommerce.digitalwallet_ledgerapi.wallet.dto.request.TransferRequest;
import com.microcommerce.digitalwallet_ledgerapi.wallet.dto.request.WithdrawRequest;
import com.microcommerce.digitalwallet_ledgerapi.wallet.dto.response.WalletResponse;
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

    @Transactional
    public void withdraw(Long walletId, WithdrawRequest request){
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("wallet not found"));

        BigDecimal currentBalance = wallet.getBalance();
        BigDecimal withdrawRequestAmount = request.amount();
        if (currentBalance.compareTo(withdrawRequestAmount) < 0 ){
            throw new RuntimeException("withdraw amount not enough");
        }

        BigDecimal newBalance = currentBalance.subtract(withdrawRequestAmount);
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setWallet(wallet);
        wallet.setBalance(newBalance);
        walletTransaction.setAmount(withdrawRequestAmount);
        walletTransaction.setType(TransactionType.WITHDRAW);
        walletTransaction.setReferenceNumber(UUID.randomUUID().toString());
        walletRepository.save(wallet);
        transcationRepo.save(walletTransaction);
    }

    @Transactional
    public void transfer(Long fromWalletId, TransferRequest request){
        Wallet fromWallet =  walletRepository.findById(fromWalletId)
                .orElseThrow(() -> new RuntimeException("from wallet not found"));
        Wallet toWallet = walletRepository.findById(request.toWalletId())
                .orElseThrow(() -> new RuntimeException("to wallet not found"));

        BigDecimal senderWalletBalance = fromWallet.getBalance();
        BigDecimal receiverWalletBalance = toWallet.getBalance();
        if (senderWalletBalance.compareTo(request.amount()) < 0){
            throw new RuntimeException("balance not enough");
        }

        BigDecimal senderWalletNewBalance = senderWalletBalance.subtract(request.amount());
        BigDecimal receiverWalletNewBalance = receiverWalletBalance.add(request.amount());
        WalletTransaction fromWalletTransaction = new WalletTransaction();
        WalletTransaction toWalletTransaction = new WalletTransaction();
        fromWalletTransaction.setWallet(fromWallet);
        toWalletTransaction.setWallet(toWallet);
        fromWallet.setBalance(senderWalletNewBalance);
        toWallet.setBalance(receiverWalletNewBalance);

        fromWalletTransaction.setType(TransactionType.TRANSFER);
        toWalletTransaction.setType(TransactionType.TRANSFER);

        toWalletTransaction.setAmount(request.amount());
        fromWalletTransaction.setAmount(request.amount());

        String uuid = UUID.randomUUID().toString();
        fromWalletTransaction.setReferenceNumber(uuid);
        toWalletTransaction.setReferenceNumber(uuid);

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);
        transcationRepo.save(fromWalletTransaction);
        transcationRepo.save(toWalletTransaction);
    }
}
