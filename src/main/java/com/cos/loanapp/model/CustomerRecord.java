package com.cos.loanapp.model;

import java.math.BigDecimal;

/**
 * The type Customer record.
 */
public record CustomerRecord(Long id, String name, String surname, BigDecimal creditLimit, BigDecimal usedCreditLimit) {
}
