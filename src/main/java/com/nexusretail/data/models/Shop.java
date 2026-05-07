package com.nexusretail.data.models;

import com.nexusretail.data.common.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "shops", indexes = {
    @Index(name = "index_shop_name", columnList = "name"),
    @Index(name = "index_shop_type", columnList = "type"),
    @Index(name = "index_shop_owner", columnList = "ownerId")
})
public class Shop extends Auditable {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // RETAIL, WHOLESALE, SERVICE, etc.

    @Column(nullable = true)
    private String phoneNo;

    @Column(nullable = true)
    private String shopPhotoUrl; // URL to shop photo

    @Column(nullable = true)
    private Long ownerId; // Reference to User who owns this shop

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShopAddress> addresses;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Role> roles;
}
