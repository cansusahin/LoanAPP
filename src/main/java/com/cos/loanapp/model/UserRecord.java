package com.cos.loanapp.model;

import com.cos.loanapp.enums.Roles;

/**
 * The type User record.
 */
public record UserRecord(Long id, String username, String password, Roles role) {

}
