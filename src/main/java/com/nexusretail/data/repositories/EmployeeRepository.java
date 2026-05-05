package com.nexusretail.data.repositories;

import com.nexusretail.data.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByShopId(Long shopId);

    Optional<Employee> findByEmail(String email);

    @Query("SELECT e FROM Employee e WHERE e.shopId = :shopId AND e.user IS NOT NULL AND e.user.id = :userId")
    Optional<Employee> findByShopIdAndUserId(@Param("shopId") Long shopId, @Param("userId") Long userId);

    @Query("SELECT e FROM Employee e WHERE e.user IS NOT NULL AND e.user.id = :userId")
    Optional<Employee> findByUserId(@Param("userId") Long userId);

    // Search and filter methods
    @Query("SELECT e FROM Employee e WHERE e.shopId = :shopId AND " +
           "(:searchTerm IS NULL OR " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.phoneNo) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Employee> findByShopIdWithSearch(@Param("shopId") Long shopId, @Param("searchTerm") String searchTerm);

    @Query("SELECT e FROM Employee e WHERE e.shopId = :shopId AND e.position = :position")
    List<Employee> findByShopIdAndPosition(@Param("shopId") Long shopId, @Param("position") String position);
}
