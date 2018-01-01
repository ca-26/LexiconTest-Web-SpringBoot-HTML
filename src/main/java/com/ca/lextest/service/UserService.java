package com.ca.lextest.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ca.lextest.db.model.User;
import com.ca.lextest.web.model.UserRegistrationDto;

public interface UserService extends UserDetailsService {
	
    User findByEmail(String email);
    User save(UserRegistrationDto registration);
    
}
