package com.cos.loanapp.model;


import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * The type Loan installment record.
 */
public record LoanInstallmentRecord(Long id, BigDecimal amount, BigDecimal paidAmount, LocalDate dueDate,
                                    LocalDate paymentDate, Boolean isPaid) {
}
