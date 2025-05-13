package com.cos.loanapp.repository;

import com.cos.loanapp.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Customer repository.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
