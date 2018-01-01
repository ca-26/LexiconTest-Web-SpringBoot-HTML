package com.ca.lextest.db.repository;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.ca.lextest.LexiconTestApplication;
import com.ca.lextest.db.model.Role;
import com.ca.lextest.db.model.User;
import com.ca.lextest.junit.categories.*;

/**
 * @author CIHAN
 * Integration Test with real DB MySQL, without persistent modifications
 * using Rolback at the end
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LexiconTestApplication.class})
@Transactional
@Rollback
@Category({Unit.class, Db.class, Slow.class})
public class UserRepositoryIT {

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
		
	private PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	// Test avec DB réelle	
	@Test
	@Transactional
	@Rollback
	public void findByEmail_UserFound() {
		//give
		Role r = new Role("ROLE_TEST");
		roleRepository.save(r);
		r = roleRepository.findByName("ROLE_TEST");
		
		User u = new User();
		u.setFirstName("John");
		u.setLastName("Doe");
		u.setEmail("john.doe@mail.com");
		u.setPassword(passwordEncoder().encode("aB123*"));
		u.setActive(1);
		u.setRoles(new HashSet<Role>(Arrays.asList(r)));
		// TODO :KO à retester
		userRepository.save(u);
		
		//when
		User found = userRepository.findByEmail(u.getEmail());
		
		//then
		assertEquals(u, found);
		
	}
	
	@Test
	public void findByEmail_UserNotFound() {
		//give
				
		//when
		User found = userRepository.findByEmail("unknown.user@mail.com");
		
		//then
		assertNull(found);		
	}	

}
