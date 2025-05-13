package com.cos.loanapp.controller;

import com.cos.loanapp.entity.Loan;
import com.cos.loanapp.entity.User;
import com.cos.loanapp.model.LoanInstallmentRecord;
import com.cos.loanapp.model.PaidLoanInstallmentRecord;
import com.cos.loanapp.service.LoanInstallmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * The type Installment controller.
 */
@RequestMapping("installment")
@RestController
public class InstallmentController {

    private final LoanInstallmentService loanInstallmentService;

    /**
     * Instantiates a new Installment controller.
     *
     * @param loanInstallmentService the loan installment service
     */
    @Autowired
    public InstallmentController(LoanInstallmentService loanInstallmentService) {
        this.loanInstallmentService = loanInstallmentService;
    }

    /**
     * List installments response entity.
     *
     * @param loanId         the loan id
     * @param authentication the authentication
     * @return the response entity
     */
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<?> listInstallments(@RequestParam Long loanId, Authentication authentication) {
        String loggedInUsername = authentication.getName();
        User user = loanInstallmentService.findUser(loggedInUsername);
        Loan loan = loanInstallmentService.findLoan(loanId);

        if ((user.getCustomer() != null && user.getCustomer().getId().equals(loan.getCustomer().getId())) || hasRole(authentication, "ROLE_ADMIN")) {
            List<LoanInstallmentRecord> loanInstallmentList = loanInstallmentService.listLoanInstallments(loanId);
            return ResponseEntity.ok(loanInstallmentList);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }


    /**
     * Pay loan installments response entity.
     *
     * @param loanId         the loan id
     * @param paymentAmount  the payment amount
     * @param authentication the authentication
     * @return the response entity
     */
    @PostMapping("/pay")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<?> payLoanInstallments(@RequestParam Long loanId, @RequestParam BigDecimal paymentAmount, Authentication authentication) {
        String loggedInUsername = authentication.getName();
        User user = loanInstallmentService.findUser(loggedInUsername);
        Loan loan = loanInstallmentService.findLoan(loanId);

        if ((user.getCustomer() != null && user.getCustomer().getId().equals(loan.getCustomer().getId())) || hasRole(authentication, "ROLE_ADMIN")) {
            PaidLoanInstallmentRecord response = loanInstallmentService.payLoanInstallments(loanId, paymentAmount);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    /**
     * Role control.
     *
     * @param authentication authentication
     * @param role           role string
     * @return boolean
     */
    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(granted -> granted.getAuthority().equals(role));
    }
}
