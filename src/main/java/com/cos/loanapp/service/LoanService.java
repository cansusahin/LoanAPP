package com.cos.loanapp.service;

import com.cos.loanapp.entity.Loan;
import com.cos.loanapp.entity.User;
import com.cos.loanapp.model.LoanRecord;

import java.math.BigDecimal;
import java.util.List;

/**
 * The interface Loan service.
 */
public interface LoanService {

    /**
     * Create loan loan record.
     *
     * @param customerId           the customer id
     * @param amount               the amount
     * @param interestRate         the interest rate
     * @param numberOfInstallments the number of installments
     * @return the loan record
     */
    public LoanRecord createLoan(Long customerId, BigDecimal amount, BigDecimal interestRate, Integer numberOfInstallments);

    /**
     * List loans list.
     *
     * @param customerId the customer id
     * @param isPaid     the is paid
     * @return the list
     */
    public List<LoanRecord> listLoans(Long customerId, Boolean isPaid);

    /**
     * Create Installments
     *
     * @param loan                 the loan
     * @param totalLoanAmount      total loan Amount
     * @param numberOfInstallments installment number
     */
    private void createInstallments(Loan loan, Integer numberOfInstallments, BigDecimal totalLoanAmount) {


    }

    /**
     * Find user user.
     *
     * @param username the username
     * @return the user
     */
    public User findUser(String username);

}
