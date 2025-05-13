package com.cos.loanapp.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import com.cos.loanapp.entity.User;
import com.cos.loanapp.enums.Roles;
import com.cos.loanapp.model.UserRecord;
import com.cos.loanapp.repository.UserRepository;
import com.cos.loanapp.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The type User service.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Instantiates a new User service.
     *
     * @param userRepository the user repository
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Loads the user by username
     *
     * @param username username
     * @return UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(), mapRolesToAuthorities(user.get().getRole()));

    }

    /**
     * Map roles to authorities collection.
     *
     * @param role the role
     * @return the collection
     */
    public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Roles role) {

        Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
        roles.add(new SimpleGrantedAuthority(role.name()));
        System.out.println(role.name());

        return roles;
    }

    /**
     * Saves user.
     *
     * @param userRegisteredRecord user record
     * @return User
     */
    @Override
    public User save(UserRecord userRegisteredRecord) {
        User user = new User();
        user.setUsername(userRegisteredRecord.username());
        user.setPassword(userRegisteredRecord.password());
        user.setRole(userRegisteredRecord.role());
        return userRepository.save(user);
    }
}
