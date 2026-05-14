package com.microcommerce.digitalwallet_ledgerapi;

import com.microcommerce.digitalwallet_ledgerapi.user.entity.User;
import com.microcommerce.digitalwallet_ledgerapi.user.repository.UserRepository;
import com.microcommerce.digitalwallet_ledgerapi.wallet.dto.request.TransferRequest;
import com.microcommerce.digitalwallet_ledgerapi.wallet.entity.Wallet;
import com.microcommerce.digitalwallet_ledgerapi.wallet.repository.WalletRepository;
import com.microcommerce.digitalwallet_ledgerapi.wallet.service.WalletService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class WalletConcurrencyTest {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testConcurentTransfer() throws InterruptedException {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());

        User senderUser = new User();
        senderUser.setUserName("test_sender_" + uniqueSuffix);
        senderUser.setPassword("test");
        senderUser.setEmail("sender_" + uniqueSuffix + "@test.com");
        senderUser = userRepository.save(senderUser);

        Wallet senderWallet = new Wallet();
        senderWallet.setUser(senderUser);
        senderWallet.setCurrency("TRY");
        senderWallet.setBalance(BigDecimal.valueOf(5000.0));
        walletRepository.save(senderWallet);

        Long userWalletId = senderWallet.getId();

        User receiverUser = new User();
        receiverUser.setUserName("test_receiver_" + uniqueSuffix);
        receiverUser.setPassword("test");
        receiverUser.setEmail("receiver_" + uniqueSuffix + "@test.com");
        receiverUser = userRepository.save(receiverUser);

        Wallet receiverWallet = new Wallet();
        receiverWallet.setUser(receiverUser);
        receiverWallet.setCurrency("TRY");
        receiverWallet.setBalance(BigDecimal.ZERO);
        walletRepository.save(receiverWallet);

        Long receiverWalletId = receiverWallet.getId();

        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        System.out.println("🚀 50 KİŞİLİK ORDU SALDIRIYA BAŞLIYOR...");

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    TransferRequest transferRequest = new TransferRequest(receiverWalletId, new BigDecimal(200.0));
                    walletService.transfer(userWalletId, transferRequest);
                } catch (Exception ex) {
                    System.out.println("🛡️ Kalkan Devrede - Engellenen işlem : " + ex.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        System.out.println("✅ SALDIRI BİTTİ. SONUÇLAR HESAPLANIYOR...");

        Wallet currentSenderWallet = walletRepository.findById(userWalletId).get();
        Wallet currentReceiverWallet = walletRepository.findById(receiverWalletId).get();

        Assertions.assertTrue(
                currentSenderWallet.getBalance().compareTo(BigDecimal.ZERO) >= 0, "Hata : gönderici bakiyesi ekside");

        BigDecimal exceptedTotal = BigDecimal.valueOf(5000.0);
        BigDecimal actualTotal = currentSenderWallet.getBalance().add(currentReceiverWallet.getBalance());

        Assertions.assertEquals(
                0,
                exceptedTotal.compareTo(actualTotal),
                "Hata: Sistemdeki toplam para değişti"
        );
    }
}