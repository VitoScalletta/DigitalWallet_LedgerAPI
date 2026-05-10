package com.microcommerce.digitalwallet_ledgerapi.wallet.entity;

import com.microcommerce.digitalwallet_ledgerapi.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;

    @Column(name = "referencenumber")
    private String referenceNumber;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
}
