package com.nexusretail.data.repositories;

import com.nexusretail.data.models.ShopAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopAddressRepository extends JpaRepository<ShopAddress, Long> {

    List<ShopAddress> findByShopId(Long shopId);

    @Query("SELECT sa FROM ShopAddress sa WHERE sa.shopId = :shopId AND sa.addressType = :addressType")
    List<ShopAddress> findByShopIdAndAddressType(@Param("shopId") Long shopId, @Param("addressType") String addressType);

    @Query("SELECT sa FROM ShopAddress sa WHERE sa.city = :city")
    List<ShopAddress> findByCity(@Param("city") String city);

    void deleteByShopId(Long shopId);
}
