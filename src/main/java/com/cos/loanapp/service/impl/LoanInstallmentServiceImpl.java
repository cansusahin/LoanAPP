package com.cos.loanapp.service.impl;

import com.cos.loanapp.entity.Customer;
import com.cos.loanapp.entity.Loan;
import com.cos.loanapp.entity.LoanInstallment;
import com.cos.loanapp.entity.User;
import com.cos.loanapp.model.LoanInstallmentRecord;
import com.cos.loanapp.model.PaidLoanInstallmentRecord;
import com.cos.loanapp.repository.CustomerRepository;
import com.cos.loanapp.repository.LoanInstallmentRepository;
import com.cos.loanapp.repository.LoanRepository;
import com.cos.loanapp.repository.UserRepository;
import com.cos.loanapp.service.LoanInstallmentService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * The type Loan installment service.
 */
@Service
public class LoanInstallmentServiceImpl implements LoanInstallmentService {

    private final LoanInstallmentRepository loanInstallmentRepository;
    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    /**
     * Instantiates a new Loan installment service.
     *
     * @param loanInstallmentRepository the loan installment repository
     * @param loanRepository            the loan repository
     * @param customerRepository        the customer repository
     * @param userRepository            the user repository
     */
    public LoanInstallmentServiceImpl(LoanInstallmentRepository loanInstallmentRepository, LoanRepository loanRepository, CustomerRepository customerRepository, UserRepository userRepository) {
        this.loanInstallmentRepository = loanInstallmentRepository;
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    /**
     * List loan installments.
     *
     * @param loanId the loan
     * @return List<LoanInstallmentRecord>
     */
    public List<LoanInstallmentRecord> listLoanInstallments(Long loanId) {
        List<LoanInstallment> installments = loanInstallmentRepository.findByLoanId(loanId);

        return installments.stream().map(installment -> new LoanInstallmentRecord(installment.getId(), installment.getAmount(), installment.getPaidAmount(), installment.getDueDate(), installment.getPaymentDate(), installment.getIsPaid())).toList();
    }

    /**
     * Pay loan installments.
     *
     * @param loanId        the loan id
     * @param paymentAmount payment amount
     * @return PaidLoanInstallmentRecord
     */
    @Transactional
    public PaidLoanInstallmentRecord payLoanInstallments(Long loanId, BigDecimal paymentAmount) {

        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found with id: " + loanId));
        List<LoanInstallment> installments = loanInstallmentRepository.findByLoanId(loanId);
        List<LoanInstallment> eligibleInstallments = installments.stream().filter(installment -> !installment.getIsPaid()).filter(installment -> installment.getDueDate().isBefore(LocalDate.now().plusMonths(3))).sorted((i1, i2) -> i1.getDueDate().compareTo(i2.getDueDate())).toList();
        Integer installmentsPaid = 0;
        BigDecimal totalAmountPaid = BigDecimal.ZERO;

        for (LoanInstallment installment : eligibleInstallments) {
            BigDecimal adjustedAmount = installment.getAmount();
            LocalDate now = LocalDate.now();
            if (now.isBefore(installment.getDueDate())) {
                long daysBefore = ChronoUnit.DAYS.between(now, installment.getDueDate());
                BigDecimal discount = adjustedAmount.multiply(BigDecimal.valueOf(0.001 * daysBefore));
                adjustedAmount = adjustedAmount.subtract(discount);
            } else if (now.isAfter(installment.getDueDate())) {
                long daysAfter = ChronoUnit.DAYS.between(installment.getDueDate(), now);
                BigDecimal penalty = adjustedAmount.multiply(BigDecimal.valueOf(0.001 * daysAfter));
                adjustedAmount = adjustedAmount.add(penalty);
            }

            if (paymentAmount.compareTo(adjustedAmount) >= 0) {
                paymentAmount = paymentAmount.subtract(adjustedAmount);
                totalAmountPaid = totalAmountPaid.add(adjustedAmount);
                installment.setPaidAmount(adjustedAmount);
                installment.setIsPaid(true);
                installment.setPaymentDate(now);
                installmentsPaid++;
            } else {
                break;
            }
        }

        boolean isLoanPaid = installments.stream().allMatch(LoanInstallment::getIsPaid);
        if (isLoanPaid) {
            loan.setIsPaid(true);
        }

        Customer customer = loan.getCustomer();
        customer.setUsedCreditLimit(customer.getUsedCreditLimit().subtract(totalAmountPaid));

        loanRepository.save(loan);
        customerRepository.save(customer);
        loanInstallmentRepository.saveAll(eligibleInstallments);

        return new PaidLoanInstallmentRecord(installmentsPaid, totalAmountPaid, isLoanPaid);
    }

    /**
     * Find user.
     *
     * @param username username
     * @return User
     */
    public User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Find loan.
     *
     * @param loanId loan id
     * @return Loan
     */
    public Loan findLoan(Long loanId) {
        return loanRepository.findById(loanId).orElseThrow(() -> new UsernameNotFoundException("Loan not found"));
    }
}
