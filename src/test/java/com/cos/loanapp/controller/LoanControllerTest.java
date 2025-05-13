package com.cos.loanapp.controller;

import com.cos.loanapp.entity.Customer;
import com.cos.loanapp.entity.User;
import com.cos.loanapp.enums.Roles;
import com.cos.loanapp.model.LoanRecord;
import com.cos.loanapp.service.LoanService;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @Test
    @WithMockUser(username = "john_doe", roles = {"CUSTOMER"})
    void testCreateLoan_ValidCustomer() throws Exception {
        // Prepare mock data
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setRole(Roles.ROLE_CUSTOMER);
        mockUser.setUsername("john_doe");
        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockUser.setCustomer(mockCustomer);
        LoanRecord mockLoanRecord = new LoanRecord(1L, new BigDecimal("1000.00"), 4, LocalDate.now(), false);

        // Mock the service layer behavior
        when(loanService.findUser("john_doe")).thenReturn(mockUser);
        when(loanService.createLoan(1L, new BigDecimal("1000"), new BigDecimal("5.5"), 12))
                .thenReturn(mockLoanRecord);

        // Perform the POST request and verify the response
        mockMvc.perform(post("/loan/create")
                        .param("customerId", "1")
                        .param("amount", "1000")
                        .param("interestRate", "5.5")
                        .param("numberOfInstallments", "12"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"CUSTOMER"})
    void testListLoans_ValidCustomer() throws Exception {
        // Prepare mock data
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setRole(Roles.ROLE_CUSTOMER);
        mockUser.setUsername("john_doe");
        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockUser.setCustomer(mockCustomer); // Customer ID 1
        LoanRecord mockLoanRecord1 = new LoanRecord(1L, new BigDecimal("1000.00"), 4, LocalDate.now(), false);
        LoanRecord mockLoanRecord2 = new LoanRecord(2L, new BigDecimal("2000.00"), 4, LocalDate.now(), false);
        List<LoanRecord> mockLoans = Arrays.asList(mockLoanRecord1, mockLoanRecord2);

        // Mock the service layer behavior
        when(loanService.findUser("john_doe")).thenReturn(mockUser);
        when(loanService.listLoans(1L, false)).thenReturn(mockLoans);

        // Perform the GET request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.get("/loan/list")
                        .param("customerId", "1")
                        .param("isPaid", "false"))
                .andExpect(status().isOk()); // Check that the status is 200 OK
    }
}
