package com.ca.lextest.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ca.lextest.LexiconTestApplication;
import com.ca.lextest.db.model.Role;
import com.ca.lextest.db.model.User;
import com.ca.lextest.db.repository.RoleRepository;
import com.ca.lextest.db.repository.UserRepository;
import com.ca.lextest.web.model.UserRegistrationDto;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import javax.persistence.RollbackException;

@Service
//@Service("userService")
public class UserServiceImpl implements UserService {
	
	private static final Logger LOG = LoggerFactory.getLogger(LexiconTestApplication.class);	
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    //@Override
    @Transactional
    public User save(UserRegistrationDto registration){
        User user = modelMapper.map(registration, User.class);
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setActive(1);
             
      //TODO :Gérer les rôles
        //user.setRoles(new HashSet<>(roleRepository.findAll()));
        Role userRole = roleRepository.findByName("ROLE_PLAYER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

        /*
        // TEST @Transactional
	        try {
	        //User user2 = modelMapper.map(user, User.class);
	        User user2 = new User();
		      user2.setFirstName(user.getFirstName());
		      user2.setLastName(user.getLastName());
		      user2.setEmail(user.getEmail());
		      user2.setPassword(user.getPassword());
		      user2.setActive(1);
		      user2.setRoles(user.getRoles());
	        userRepository.save(user2);
	        logger.warn("save user2 end - Id="+user2.getId());
	
	        logger.warn("save user start - Id="+user.getId());
	        //user.setId(null);
	        //user.setEmail("");
	        userRepository.save(user);        
	        logger.warn("save user end - Id="+user.getId());
	        
	        } catch (RollbackException e) {
	            logger.warn("error catched : RollbackException");
	        }
        */

        LOG.debug("----->>>> UserServiceImpl : save User :  |{}|", user.toString());
        return userRepository.save(user);
    }

    @Override
    //Spring Security Login Check
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("{email.address.unknown}");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}