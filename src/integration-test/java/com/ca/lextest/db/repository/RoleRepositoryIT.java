package com.ca.lextest.db.repository;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ca.lextest.LexiconTestApplication;
import com.ca.lextest.db.model.Role;
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
@ActiveProfiles("integration-test")
@Category({Unit.class, Db.class, Slow.class})
public class RoleRepositoryIT {
	
	@Autowired
	private RoleRepository roleRepository;
	
	// Test avec DB réelle
	@Test
	public void testFindByName_RoleFound() {
		//give
		Role r = new Role("ROLE_TEST");
		roleRepository.save(r);
		
		//when
		Role found = roleRepository.findByName("ROLE_TEST");
		
		//then
		assertEquals(r.getName(), found.getName());
	}
	
	@Test
	public void findByName_RoleNotFound() {
		//give
		
		//when
		Role found = roleRepository.findByName("ROLE_UNKNOWN");
		
		//then
		assertNull(found);
	}

}
