package com.microcommerce.digitalwallet_ledgerapi.wallet.service;

import com.microcommerce.digitalwallet_ledgerapi.user.entity.User;
import com.microcommerce.digitalwallet_ledgerapi.user.repository.UserRepository;
import com.microcommerce.digitalwallet_ledgerapi.wallet.dto.CreateWalletRequest;
import com.microcommerce.digitalwallet_ledgerapi.wallet.dto.WalletResponse;
import com.microcommerce.digitalwallet_ledgerapi.wallet.entity.Wallet;
import com.microcommerce.digitalwallet_ledgerapi.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

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
}
