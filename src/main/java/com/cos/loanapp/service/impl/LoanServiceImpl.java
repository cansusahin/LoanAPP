package com.cos.loanapp.service.impl;

import com.cos.loanapp.entity.Customer;
import com.cos.loanapp.entity.Loan;
import com.cos.loanapp.entity.LoanInstallment;
import com.cos.loanapp.entity.User;
import com.cos.loanapp.model.LoanRecord;
import com.cos.loanapp.repository.CustomerRepository;
import com.cos.loanapp.repository.LoanInstallmentRepository;
import com.cos.loanapp.repository.LoanRepository;
import com.cos.loanapp.repository.UserRepository;
import com.cos.loanapp.service.LoanService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Loan service.
 */
@Service
public class LoanServiceImpl implements LoanService {

    private final CustomerRepository customerRepository;
    private final LoanRepository loanRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;
    private final UserRepository userRepository;

    /**
     * Instantiates a new Loan service.
     *
     * @param customerRepository        the customer repository
     * @param loanRepository            the loan repository
     * @param loanInstallmentRepository the loan installment repository
     * @param userRepository            the user repository
     */
    public LoanServiceImpl(CustomerRepository customerRepository, LoanRepository loanRepository, LoanInstallmentRepository loanInstallmentRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.loanRepository = loanRepository;
        this.loanInstallmentRepository = loanInstallmentRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a loan.
     *
     * @param customerId           the customer id
     * @param amount               the amount
     * @param interestRate         the interest rate
     * @param numberOfInstallments installment number
     * @return LoanRecord
     */
    public LoanRecord createLoan(Long customerId, BigDecimal amount, BigDecimal interestRate, Integer numberOfInstallments) {

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));

        BigDecimal totalLoanAmount = amount.multiply(BigDecimal.ONE.add(interestRate));
        if (customer.getUsedCreditLimit().add(totalLoanAmount).compareTo(customer.getCreditLimit()) > 0) {
            throw new RuntimeException("Insufficient credit limit");
        }

        Loan loan = new Loan();
        loan.setCustomer(customer);
        loan.setLoanAmount(amount);
        loan.setNumberOfInstallment(numberOfInstallments);
        loan.setCreateDate(LocalDate.now());
        loan.setIsPaid(false);

        Loan savedLoan = loanRepository.save(loan);
        createInstallments(savedLoan, numberOfInstallments, totalLoanAmount);
        customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(totalLoanAmount));
        customerRepository.save(customer);
        return new LoanRecord(savedLoan.getId(), savedLoan.getLoanAmount(), savedLoan.getNumberOfInstallment(), savedLoan.getCreateDate(), savedLoan.getIsPaid());
    }

    /**
     * Create a loan.
     *
     * @param customerId the customer id
     * @param isPaid     is paid boolean
     * @return List<LoanRecord>
     */
    public List<LoanRecord> listLoans(Long customerId, Boolean isPaid) {
        List<Loan> loans;

        if (isPaid != null) {
            loans = loanRepository.findByCustomerIdAndIsPaid(customerId, isPaid);
        } else {
            loans = loanRepository.findByCustomerId(customerId);
        }
        return loans.stream().map(loan -> new LoanRecord(loan.getId(), loan.getLoanAmount(), loan.getNumberOfInstallment(), loan.getCreateDate(), loan.getIsPaid())).toList();
    }

    private void createInstallments(Loan loan, Integer numberOfInstallments, BigDecimal totalLoanAmount) {
        BigDecimal installmentAmount = totalLoanAmount.divide(BigDecimal.valueOf(numberOfInstallments), BigDecimal.ROUND_HALF_UP);
        LocalDate dueDate = LocalDate.now().plusMonths(1).withDayOfMonth(1);

        List<LoanInstallment> installments = new ArrayList<>();
        for (int i = 0; i < numberOfInstallments; i++) {
            LoanInstallment installment = new LoanInstallment();
            installment.setLoan(loan);
            installment.setAmount(installmentAmount);
            installment.setPaidAmount(BigDecimal.ZERO);
            installment.setDueDate(dueDate.plusMonths(i));
            installment.setIsPaid(false);
            installments.add(installment);
        }
        loanInstallmentRepository.saveAll(installments);
    }

    /**
     * Find user.
     *
     * @param username username
     * @return User
     */
    @Override
    public User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}