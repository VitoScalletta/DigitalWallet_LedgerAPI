package com.microcommerce.digitalwallet_ledgerapi.wallet.entity;

import com.microcommerce.digitalwallet_ledgerapi.common.entity.BaseEntity;
import com.microcommerce.digitalwallet_ledgerapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "currency")
    private String currency;


    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @Version
    private Integer version;
}
