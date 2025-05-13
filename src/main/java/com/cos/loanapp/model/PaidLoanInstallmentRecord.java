package com.cos.loanapp.model;

import java.math.BigDecimal;

/**
 * The type Paid loan installment record.
 */
public record PaidLoanInstallmentRecord(Integer installmentsPaid, BigDecimal totalAmountPaid, Boolean isLoanPaid) {
}
