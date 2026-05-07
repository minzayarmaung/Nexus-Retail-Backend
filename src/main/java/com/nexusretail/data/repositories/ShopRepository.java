package com.nexusretail.data.repositories;

import com.nexusretail.data.models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    List<Shop> findByOwnerId(Long ownerId);

    Optional<Shop> findByName(String name);

    @Query("SELECT s FROM Shop s WHERE s.type = :type")
    List<Shop> findByType(@Param("type") String type);

    @Query("SELECT s FROM Shop s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Shop> findByNameContaining(@Param("searchTerm") String searchTerm);
}