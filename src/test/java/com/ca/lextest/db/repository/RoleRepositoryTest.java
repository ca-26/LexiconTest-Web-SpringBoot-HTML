package com.ca.lextest.db.repository;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ca.lextest.db.model.Role;
import com.ca.lextest.junit.categories.*;

//@DataJpaTest /* To use SpringBoot default H2 inmemory DB */
//@SpringBootTest(classes = {LexiconTestApplication.class})
//@TestPropertySource(locations = "classpath:/persistence-test.properties")
/**
 * @author CIHAN
 * Test with in memory DB H2(mode MySQL) loaded with MySQL Datas
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Category({Unit.class, Db.class, Fast.class})
@Transactional /*Mandatory because custom Persistence Unit mysqlPU is transactional and @PersistenceContext also by default */
public class RoleRepositoryTest {

	@PersistenceContext(unitName="mysqlPU")
	private EntityManager mysqlEntityManager;	
	@Autowired
	private RoleRepository roleRepository;
	
	// Test avec DB in memorry H2
	@Test
	public void whenCreatedRoleAndfindByName_thenRoleFound() {
	  //give
		Role r = new Role("ROLE_TEST");
		//mysqlEntityManager.persist(r);
		//mysqlEntityManager.flush();
		//or
		roleRepository.save(r);
		
	  //when
		Role found = roleRepository.findByName(r.getName());
		
	  //then
		assertEquals(r.getName(), found.getName());	
	}
	
	@Test (expected = DataIntegrityViolationException.class)
	public void whenCreategRole_withExistingRoleName_thenErrorConstraintViolation() throws Exception{
		//give
		Role r = new Role("ROLE_TEST");
		roleRepository.save(r);
		
		//when
		Role r2 = new Role("ROLE_TEST");
		roleRepository.save(r2);
		
		//then	
	}
	
	@Test
	public void whenFindByName_withUnknownRole_thenRoleNotFound() {
		//give
		
		//when
		Role found = roleRepository.findByName("ROLE_UNKNOWN");
		
		//then
		assertNull(found);
	}

}
