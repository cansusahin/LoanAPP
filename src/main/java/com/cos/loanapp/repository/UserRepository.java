package com.cos.loanapp.repository;

import com.cos.loanapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface User repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find by username user.
     *
     * @param username the username
     * @return the user
     */
    Optional<User> findByUsername(String username);
}
