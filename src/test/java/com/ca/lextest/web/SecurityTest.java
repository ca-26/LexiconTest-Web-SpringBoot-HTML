package com.ca.lextest.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ca.lextest.junit.categories.*;

/**
 * @author CIHAN
 * Test Spring Security Roles
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
public class SecurityTest {

	//private static final Logger LOG = LoggerFactory.getLogger(LexiconTestApplication.class);
	@PersistenceContext(unitName="mysqlPU")
	private EntityManager mysqlEntityManager;
//	@Autowired
//	private RoleRepository roleRepository;
//	@Autowired
//	private UserRepository userRepository;	
    @Autowired
    private WebApplicationContext wac;    
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    private MockMvc mockMvc;
    private static boolean setUpIsDone = false;
    
	
    @Before
    public void setup()  throws Exception {
    	// run before each test
    	this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
    			.addFilter(springSecurityFilterChain).build();
    	//this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    	
    	// run once only
		if (setUpIsDone) {
		    return;
		}
//	    Role role_player = new Role("ROLE_PLAYER");
//		mySqlConfig.mysqlEntityManager().persist(role_player);
//		// Flush for transactional requests
//		//mySqlConfig.mysqlEntityManager().flush();
//	    Role role_admin = new Role("ROLE_ADMIN");
//		mySqlConfig.mysqlEntityManager().persist(role_admin);
//				
//		User user_player = new User();
//		user_player.setFirstName("User");
//		user_player.setLastName("Player");
//		user_player.setEmail("player@mail.com");
//		user_player.setPassword(passwordEncoder().encode("aB123*"));
//		user_player.setActive(1);
//		user_player.setRoles(new HashSet<Role>(Arrays.asList(role_player)));        
//		userRepository.save(user_player);
//		
//		User user_admin = new User();
//		user_admin.setFirstName("User");
//		user_admin.setLastName("Admin");
//		user_admin.setEmail("admin@mail.com");
//		user_admin.setPassword(passwordEncoder().encode("aB123*"));
//		user_admin.setActive(1);
//		user_admin.setRoles(new HashSet<Role>(Arrays.asList(role_admin)));        
//		userRepository.save(user_admin);
		
		//System.out.println("=======================================");
		//System.out.println(userRepository.findAll().toString());
		//System.out.println("=======================================");
				
		setUpIsDone = true;
    }

    @Test
    public void whenAccessRootPage_withAnyUser_thenAccessOk() throws Exception {
    	mockMvc.perform(get("/")
	            .with(csrf()))
    			.andExpect(status().isOk());
        
        mockMvc.perform(get("/")
	            .with(csrf()).with(user("player@mail.com").password("aB123*").roles("PLAYER")))
        	   .andExpect(status().isOk());
        
        mockMvc.perform(get("/")
	            .with(csrf()).with(user("admin@mail.com").password("aB123*").roles("ADMIN")))
        	   .andExpect(status().isOk());   
    }
    
    @Test
    public void whenAccessLoginPage_withAnyUser_thenAccessOk() throws Exception {
    	mockMvc.perform(get("/login")
	            .with(csrf()))
    			.andExpect(status().isOk());
        
        mockMvc.perform(get("/login")
	            .with(csrf()).with(user("player@mail.com").password("aB123*").roles("PLAYER")))
        	   .andExpect(status().isOk());
        
        mockMvc.perform(get("/login")
	            .with(csrf()).with(user("admin@mail.com").password("aB123*").roles("ADMIN")))
        	   .andExpect(status().isOk()); 
    }
    
    @Test
    public void whenAccessGameHomePage_withAnyUser_thenAccessOk() throws Exception {
    	//mockMvc.perform(get("/game/home")).andExpect(status().isUnauthorized());
    	mockMvc.perform(get("/game/home")
	            .with(csrf()))
    			.andExpect(status().isFound())
    			.andExpect(redirectedUrl("http://localhost/login"));
        
        mockMvc.perform(get("/game/home")
	            .with(csrf()).with(user("player@mail.com").password("aB123*").roles("PLAYER")))
 	   			.andExpect(status().isOk());
        
        mockMvc.perform(get("/game/home")
	            .with(csrf()).with(user("admin@mail.comm").password("aB123*").roles("ADMIN")))
        	   .andExpect(status().isOk()); 
    }
    
    @Test
    public void whenAccessAdminHomePage_withAnyUser_thenAccessOk() throws Exception {
    	//mockMvc.perform(get("/admin/home")).andExpect(status().isForbidden());
    	mockMvc.perform(get("/admin/home")
	            .with(csrf()))
    			.andExpect(status().isFound())
    			.andExpect(redirectedUrl("http://localhost/login"));
        
        mockMvc.perform(get("/admin/home")
	            .with(csrf()).with(user("player@mail.com").password("aB123*").roles("PLAYER")))
        	   .andExpect(status().isForbidden());
        
        mockMvc.perform(get("/admin/home")
	            .with(csrf()).with(user("admin@mail.comm").password("aB123*").roles("ADMIN")))
        	   .andExpect(status().isOk()); 
    }
    
	@After
	public void tearDown() throws Exception {
		// run after each test
	}
    
}