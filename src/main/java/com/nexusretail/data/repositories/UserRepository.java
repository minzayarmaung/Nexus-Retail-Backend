package com.nexusretail.data.repositories;
import com.nexusretail.common.exception.UserNotFoundException;
import com.nexusretail.data.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    @Query("select u from User u join fetch u.office where u.username = :username")
    java.util.Optional<User> findByUsernameWithOffice(@Param("username") String username);

    default User findOneWithNotFoundDetection(final String username) {
        return findByUsernameWithOffice(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
}