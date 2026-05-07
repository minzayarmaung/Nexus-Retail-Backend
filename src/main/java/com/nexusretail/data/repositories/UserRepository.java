package com.nexusretail.data.repositories;

import com.nexusretail.common.dto.response.PaginatedApiResponse;
import com.nexusretail.data.models.User;
import com.nexusretail.feature.user.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.shop LEFT JOIN FETCH u.role")
    Page<User> findAllWithShopAndRole(Pageable pageable);
}

