package com.microcommerce.digitalwallet_ledgerapi.user.entity;

import com.microcommerce.digitalwallet_ledgerapi.common.entity.BaseEntity;
import com.microcommerce.digitalwallet_ledgerapi.wallet.entity.Wallet;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false,unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    List<Wallet> wallets;

}
