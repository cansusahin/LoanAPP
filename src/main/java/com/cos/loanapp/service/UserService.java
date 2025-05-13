package com.cos.loanapp.service;

import com.cos.loanapp.entity.User;
import com.cos.loanapp.model.UserRecord;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * The interface User service.
 */
public interface UserService extends UserDetailsService {

    /**
     * Save user.
     *
     * @param userRecordRegisteredDTO the user record registered dto
     * @return the user
     */
    User save(UserRecord userRecordRegisteredDTO);
}
