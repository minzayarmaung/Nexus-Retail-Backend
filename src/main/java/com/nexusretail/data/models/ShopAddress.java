package com.nexusretail.data.models;

import com.nexusretail.data.common.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "shop_addresses", indexes = {
    @Index(name = "index_shop_address_shop", columnList = "shopId"),
    @Index(name = "index_shop_address_type", columnList = "addressType")
})
public class ShopAddress extends Auditable {

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String addressType; // HEADQUARTER, BRANCH, WAREHOUSE, etc.

    @Column(nullable = true)
    private String city;

    @Column(nullable = true)
    private String state;

    @Column(nullable = true)
    private String postalCode;

    @Column(nullable = true)
    private String country;

    @Column(nullable = true)
    private Double latitude; // For location services

    @Column(nullable = true)
    private Double longitude; // For location services

    @Column(nullable = false)
    private Long shopId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopId", referencedColumnName = "id", insertable = false, updatable = false)
    private Shop shop;
}
