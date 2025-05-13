package com.cos.loanapp.controller;

import com.cos.loanapp.entity.Customer;
import com.cos.loanapp.entity.Loan;
import com.cos.loanapp.entity.User;
import com.cos.loanapp.enums.Roles;
import com.cos.loanapp.model.LoanInstallmentRecord;
import com.cos.loanapp.model.LoanRecord;
import com.cos.loanapp.model.PaidLoanInstallmentRecord;
import com.cos.loanapp.service.LoanInstallmentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class LoanInstallmentControllerTest {

    @MockBean
    private LoanInstallmentService installmentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "john_doe", roles = {"CUSTOMER"})
    void testListInstallments_ValidCustomer() throws Exception {
        // Prepare mock data
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setRole(Roles.ROLE_CUSTOMER);
        mockUser.setUsername("john_doe");
        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockUser.setCustomer(mockCustomer);
        Loan mockLoan = new Loan();
        mockLoan.setCustomer(mockCustomer);
        mockLoan.setId(1L);
        LoanInstallmentRecord mockInstallment1 = new LoanInstallmentRecord(1L, new BigDecimal("100.00"), new BigDecimal("20.00"), LocalDate.now(), LocalDate.now(), false);
        LoanInstallmentRecord mockInstallment2 = new LoanInstallmentRecord(2L, new BigDecimal("200.00"), new BigDecimal("100.00"), LocalDate.now(), LocalDate.now(), false);
        List<LoanInstallmentRecord> mockInstallments = Arrays.asList(mockInstallment1, mockInstallment2);

        // Mock the service layer behavior
        when(installmentService.findUser("john_doe")).thenReturn(mockUser);
        when(installmentService.findLoan(1L)).thenReturn(mockLoan);
        when(installmentService.listLoanInstallments(1L)).thenReturn(mockInstallments);

        // Perform the GET request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.get("/installment/list")
                        .param("loanId", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Check that the status is 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1)) // Check the first installment id
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount").value(100.00)) // Check the first installment amount
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2)) // Check the second installment id
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].amount").value(200.00)); // Check the second installment amount
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"CUSTOMER"})
    void testPayLoanInstallments_ValidCustomer() throws Exception {
        // Prepare mock data
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setRole(Roles.ROLE_CUSTOMER);
        mockUser.setUsername("john_doe");
        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockUser.setCustomer(mockCustomer);
        Loan mockLoan = new Loan();
        mockLoan.setCustomer(mockCustomer);
        mockLoan.setId(1L); // Loan with Customer ID 1
        PaidLoanInstallmentRecord mockPaidInstallment = new PaidLoanInstallmentRecord(1, BigDecimal.valueOf(100.00), true);

        // Mock the service layer behavior
        when(installmentService.findUser("john_doe")).thenReturn(mockUser);
        when(installmentService.findLoan(1L)).thenReturn(mockLoan);
        when(installmentService.payLoanInstallments(1L, BigDecimal.valueOf(100.00))).thenReturn(mockPaidInstallment);

        // Perform the POST request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.post("/installment/pay")
                        .param("loanId", "1")
                        .param("paymentAmount", "100.00"))
                .andExpect(MockMvcResultMatchers.status().isOk()); // Check that the status is 200 OK
    }

}
