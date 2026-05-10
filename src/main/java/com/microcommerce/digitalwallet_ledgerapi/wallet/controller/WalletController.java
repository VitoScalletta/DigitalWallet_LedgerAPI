package com.microcommerce.digitalwallet_ledgerapi.wallet.controller;

import com.microcommerce.digitalwallet_ledgerapi.wallet.dto.request.CreateWalletRequest;
import com.microcommerce.digitalwallet_ledgerapi.wallet.dto.request.DepositRequest;
import com.microcommerce.digitalwallet_ledgerapi.wallet.dto.request.WithdrawRequest;
import com.microcommerce.digitalwallet_ledgerapi.wallet.dto.response.WalletResponse;
import com.microcommerce.digitalwallet_ledgerapi.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createWallet(@Valid @RequestBody CreateWalletRequest request) {
        walletService.createWallet(request);
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<WalletResponse> findAllWallets(@PathVariable Long userId) {
        return walletService.findAllByUserId(userId);
    }

    @PostMapping("/{walletId}/deposit")
    @ResponseStatus(HttpStatus.OK)
    public void depositWallet(@PathVariable Long walletId, @Valid @RequestBody DepositRequest request) {
        walletService.deposit(walletId, request);
    }

    @PostMapping("/{walletId}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public void withdrawWallet(@PathVariable Long walletId, @Valid @RequestBody WithdrawRequest request) {
        walletService.withdraw(walletId, request);
    }
}
