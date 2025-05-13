package com.cos.loanapp.service;

import com.cos.loanapp.entity.Loan;
import com.cos.loanapp.entity.User;
import com.cos.loanapp.model.LoanInstallmentRecord;
import com.cos.loanapp.model.PaidLoanInstallmentRecord;
import java.math.BigDecimal;
import java.util.List;

/**
 * The interface Loan installment service.
 */
public interface LoanInstallmentService {
    /**
     * List loan installments list.
     *
     * @param loanId the loan id
     * @return the list
     */
    public List<LoanInstallmentRecord> listLoanInstallments(Long loanId);

    /**
     * Pay loan installments paid loan installment record.
     *
     * @param loanId        the loan id
     * @param paymentAmount the payment amount
     * @return the paid loan installment record
     */
    public PaidLoanInstallmentRecord payLoanInstallments(Long loanId, BigDecimal paymentAmount);

    /**
     * Find user user.
     *
     * @param username the username
     * @return the user
     */
    public User findUser(String username);

    /**
     * Find loan loan.
     *
     * @param loanId the loan id
     * @return the loan
     */
    public Loan findLoan(Long loanId);
}
