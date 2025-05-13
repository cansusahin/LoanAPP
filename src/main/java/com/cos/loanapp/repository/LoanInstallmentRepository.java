package com.cos.loanapp.repository;

import com.cos.loanapp.entity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Loan installment repository.
 */
@Repository
public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {

    /**
     * Find by loan id list.
     *
     * @param loanId the loan id
     * @return the list
     */
    List<LoanInstallment> findByLoanId(Long loanId);
}
