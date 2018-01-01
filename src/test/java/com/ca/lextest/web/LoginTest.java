package com.ca.lextest.web;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ca.lextest.db.model.Role;
import com.ca.lextest.db.model.User;
import com.ca.lextest.db.repository.RoleRepository;
import com.ca.lextest.db.repository.UserRepository;
import com.ca.lextest.junit.categories.*;


/**
 * @author CIHAN
 * Test Spring Security Authentification with H2 in Memory DB
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {LexiconTestApplication.class, H2TestProfileJPAConfigInFile.class})
//@SpringBootTest(classes = {LexiconTestApplication.class})
@SpringBootTest
//@DataJpaTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("test")
//@PropertySource("classpath:/persistence-test.properties")
//@SpringBootTest(classes = {LexiconTestApplication.class, WebMvcConfig.class})
//@ContextConfiguration(classes = {TestContext.class, WebAppContext.class})
@Category({Unit.class, Security.class, Slow.class})
@Transactional /*Mandatory because custom Persistence Unit mysqlPU is transactional and @PersistenceContext also by default */
public class LoginTest {

	//private static final Logger LOG = LoggerFactory.getLogger(LexiconTestApplication.class);
	@PersistenceContext(unitName="mysqlPU")
	private EntityManager mysqlEntityManager;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;	
    @Autowired
    private WebApplicationContext wac;    
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    private MockMvc mockMvc;
	private PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
    private static boolean setUpIsDone = false;
    
	
    @Before
    public void setup()  throws Exception {
      //#### run before each test
    	mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
    			.addFilter(springSecurityFilterChain).build();
    	//mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    	
    	Role r = new Role("ROLE_PLAYER");
	    roleRepository.save(r);
	    //mysqlEntityManager.persist(r);
		// Flush for transactional requests
		//mysqlEntityManager.flush();
				
		User u = new User();
		u.setFirstName("Tom");
		u.setLastName("Cruze");
		u.setEmail("tom.cruze@mail.com");
		u.setPassword(passwordEncoder().encode("aB123*"));
		u.setActive(1);
	    u.setRoles(new HashSet<Role>(Arrays.asList(r)));
		userRepository.saveAndFlush(u);
	    //mysqlEntityManager.persist(u);
		//mysqlEntityManager.flush();
		    	
    	//LOG.debug("----->>>> setUp Is Done"); 
    	
      //#### run once only before all tests
		if (setUpIsDone) {
		    return;
		}		
		
		setUpIsDone = true;
    	//LOG.debug("----->>>> setUp Once Is Done");    	
    }

    @Test
    public void whenUserLogin_withBlankCredentials_thenErrorMessage() throws Exception {
        mockMvc.perform(
        		post("/login")
		            .with(csrf())
		            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
		            .param("email", "")
		            .param("password", "")
		            .param("remember-me", "off"))
        
            .andExpect(status().isFound()).andExpect(redirectedUrl("/login?error=true"));
    }
 
    @Test
    public void whenUserLogin_withWrongEmail_thenErrorMessage() throws Exception {
        mockMvc.perform(
        		post("/login")
	                .with(csrf())
	                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .param("email", "unknown-user@mail.com")
	                .param("password", "aB123*")
	                .param("remember-me", "off"))
        
            .andExpect(status().isFound()).andExpect(redirectedUrl("/login?error=true"));
    }
 
    @Test
    public void whenUserLogin_withWrongPassword_thenErrorMessage() throws Exception {
        mockMvc.perform(
        		post("/login")
	                .with(csrf())
	                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .param("email", "tom.cruze@mail.com")
	                .param("password", "wrongPassword")
	                .param("remember-me", "off"))
        
            .andExpect(status().isFound()).andExpect(redirectedUrl("/login?error=true"));
    }
    /**/
    @Test
    public void whenUserLogin_withValidCredentialsAndRememberMeNotChecked_thenAccessGameHomePageAndNoRMTokenInDb() throws Exception {
    	//LOG.debug("----->>>> {}", userRepository.findAll().toString());
        mockMvc.perform(
        		post("/login")
	                .with(csrf())
	                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .param("email", "tom.cruze@mail.com")
	                .param("password", "aB123*")
	                .param("remember-me", "off"))
        
            .andExpect(status().isFound()).andExpect(redirectedUrl("/game/home"));
        
      // Check Remember-Me Persistent Connection token in DB
 		Query query = mysqlEntityManager.createNativeQuery("SELECT COUNT(*) FROM lexicontest.persistent_logins where username='tom.cruze@mail.com'");
 		int nbRememberMeTokenInDB = ((BigInteger) query.getSingleResult()).intValue();
 		
 		assertEquals(0, nbRememberMeTokenInDB);	
    }
       
    @Test
    public void whenUserLogin_withValidCredentialsAndRememberMeChecked_thenAccessGameHomePageAndRMTokenOkInDb() throws Exception {
    	//LOG.debug("----->>>> {}", userRepository.findAll().toString());
      // Login the user
        mockMvc.perform(
        		post("/login")
	                .with(csrf())
	                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .param("email", "tom.cruze@mail.com")
	                .param("password", "aB123*")
	                .param("remember-me", "on"))
        
            .andExpect(status().isFound()).andExpect(redirectedUrl("/game/home"));
        
      // Check Remember-Me Persistent Connection token in DB
		Query query = mysqlEntityManager.createNativeQuery("SELECT COUNT(*) FROM lexicontest.persistent_logins where username='tom.cruze@mail.com'");
		int nbRememberMeTokenInDB = ((BigInteger) query.getSingleResult()).intValue();
		
		assertEquals(1, nbRememberMeTokenInDB);	
    }
    
	@After
	public void tearDown() throws Exception {
		//#### run after each test
		
//		System.out.println("==========================================");
//		Query query = mysqlEntityManager.createNativeQuery("SELECT COUNT(*) FROM lexicontest.persistent_logins");
//		System.out.println("==========================================");
//		//Object[] author = (Object[]) q.getSingleResult();
//		int count = ((BigInteger) query.getSingleResult()).intValue();
//		System.out.println("==========================================");
//        System.out.println(count);
//		System.out.println("==========================================");
	}
    
}