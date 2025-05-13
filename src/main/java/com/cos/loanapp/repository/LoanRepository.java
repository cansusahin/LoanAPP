package com.cos.loanapp.repository;

import com.cos.loanapp.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Loan repository.
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    /**
     * Find by customer id list.
     *
     * @param customerId the customer id
     * @return the list
     */
    List<Loan> findByCustomerId(Long customerId);

    /**
     * Find by customer id and is paid list.
     *
     * @param customerId the customer id
     * @param isPaid     the is paid
     * @return the list
     */
    List<Loan> findByCustomerIdAndIsPaid(Long customerId, Boolean isPaid);

}
