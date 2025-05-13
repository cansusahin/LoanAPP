package com.cos.loanapp.controller;

import com.cos.loanapp.entity.User;
import com.cos.loanapp.model.LoanRecord;
import com.cos.loanapp.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


/**
 * The type Loan controller.
 */
@RequestMapping("loan")
@RestController
public class LoanController {

    @Autowired
    private LoanService loanService;

    /**
     * Create loan response entity.
     *
     * @param customerId           the customer id
     * @param amount               the amount
     * @param interestRate         the interest rate
     * @param numberOfInstallments the number of installments
     * @param authentication       the authentication
     * @return the response entity
     */
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<?> createLoan(@RequestParam Long customerId, @RequestParam BigDecimal amount, @RequestParam BigDecimal interestRate, @RequestParam Integer numberOfInstallments, Authentication authentication) {
        String loggedInUsername = authentication.getName();
        User user = loanService.findUser(loggedInUsername);
        if ((user.getCustomer() != null && user.getCustomer().getId().equals(customerId))|| hasRole(authentication, "ROLE_ADMIN")) {
            LoanRecord loan = loanService.createLoan(customerId, amount, interestRate, numberOfInstallments);
            return ResponseEntity.ok(loan);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    /**
     * List loans response entity.
     *
     * @param customerId the customer id
     * @param isPaid     the is paid
     * @return the response entity
     */
    @GetMapping("/list")
    public ResponseEntity<?> listLoans(@RequestParam Long customerId, @RequestParam(required = false) Boolean isPaid, Authentication authentication) {
        String loggedInUsername = authentication.getName();
        User user = loanService.findUser(loggedInUsername);
        if ((user.getCustomer() != null && user.getCustomer().getId().equals(customerId))|| hasRole(authentication, "ROLE_ADMIN")) {
            List<LoanRecord> loans = loanService.listLoans(customerId, isPaid);
            return ResponseEntity.ok(loans);
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
