package com.cos.loanapp.model;


import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * The type Loan record.
 */
public record LoanRecord(Long id, BigDecimal loanAmount, Integer numberOfInstallment, LocalDate createDate,
                         Boolean isPaid) {
}
