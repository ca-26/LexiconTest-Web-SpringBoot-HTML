package com.ca.lextest.db.repository;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ca.lextest.LexiconTestApplication;
import com.ca.lextest.db.model.Role;
import com.ca.lextest.db.model.User;
import com.ca.lextest.junit.categories.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LexiconTestApplication.class})
@ActiveProfiles("test")
@Category({Unit.class, Db.class, Fast.class})
@Transactional /*Mandatory because custom Persistence Unit mysqlPU is transactional and @PersistenceContext also by default */
public class UserRepositoryTest {

	@PersistenceContext(unitName="mysqlPU")
	private EntityManager mysqlEntityManager;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;	
	private PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
 
	 /*@BeforeClass
	 public static void setUpClass() throws Exception {
		 // run once only before any of the tests		 
	 }*/
	  
	// Test avec DB in memorry H2
	@Test
	public void whenCreateUserAndFindByEmail_thenUserFound() {
	  //give
		Role r = new Role("ROLE_PLAYER");
		mysqlEntityManager.persist(r);
		// Flush for transactional requests
		//mysqlEntityManager.flush();
		//roleRepository.save(r);
		//roleRepository.flush();
		
		User u = new User();
		u.setFirstName("Tom");
		u.setLastName("Cruze");
		u.setEmail("tom.cruze@mail.com");
		u.setPassword(passwordEncoder().encode("aB123*"));
		u.setActive(1);
        u.setRoles(new HashSet<Role>(Arrays.asList(r)));        
		userRepository.save(u);
		//userRepository.flush();
		
	  //when
		User found = userRepository.findByEmail(u.getEmail());
		
	  //then
		assertEquals(u, found);
		System.out.println(roleRepository.findAll().toString());
	}
	
	@Test
	public void whenFindByEmail_withUnknownEmail_thenUserNotFound() {
	  //give
				
	  //when
		User found = userRepository.findByEmail("unknown.user@mail.com");
		
	  //then
		assertNull(found);		
	}	

}
