package com.ca.lextest.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.ca.lextest.db.config.MySqlConfig;
import com.ca.lextest.db.model.Role;
import com.ca.lextest.db.model.User;
import com.ca.lextest.db.repository.RoleRepository;
import com.ca.lextest.db.repository.UserRepository;
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

// @Transactional mandatory because using @PersistenceContext which is transactional by default and custom "mysqlPU" also
//https://stackoverflow.com/questions/32269192/spring-no-entitymanager-with-actual-transaction-available-for-current-thread
@Transactional /*Mandatory because custom Persistence Unit mysqlPU is transactional and @PersistenceContext also by default */
@Category({Unit.class, Security.class, Slow.class})
public class RegistrationTest {

	//private static final Logger LOG = LoggerFactory.getLogger(LexiconTestApplication.class);
	@PersistenceContext(unitName="mysqlPU")
	private EntityManager mysqlEntityManager;
	@Autowired
	MySqlConfig mySqlConfig;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;	
    @Autowired
    private WebApplicationContext wac;    
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    
    private MockMvc mockMvc;
    private static boolean setUpIsDone = false;
    
	private PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
    
	
    @Before
    public void setup()  throws Exception {
      //#### run before each test
    	this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
    			.addFilter(springSecurityFilterChain).build();
    	//this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    	
    	Role role_player = new Role("ROLE_PLAYER");
		roleRepository.save(role_player);
		//mysqlEntityManager.persist(role_player);
		//mysqlEntityManager.persist(role_player);
		//mysqlEntityManager.flush();
		
		//mysqlEntityManager.flush();
		//Role roleAdmin = new Role("ROLE_ADMIN");
		//mysqlEntityManager.persist(roleAdmin);
		
		User user_existing = new User();
		user_existing.setFirstName("Existing");
		user_existing.setLastName("USER");
		user_existing.setEmail("existing.user@mail.com");
		user_existing.setPassword(passwordEncoder().encode("aB123*"));
		user_existing.setActive(1);
		user_existing.setRoles(new HashSet<Role>(Arrays.asList(role_player)));        
		userRepository.save(user_existing);
		
      //####  run once only before all tests
		if (setUpIsDone) {
		    return;
		}
		
        //LOG.debug("----->>>> {}", roleRepository.findAll().toString());
        setUpIsDone = true;
    }

    @Test
    public void whenAccessRegistrationPage_withAnyUser_thenAccessOk() throws Exception {
    	mockMvc.perform(get("/registration")
	            .with(csrf()))
    			.andExpect(status().isOk());
        
        mockMvc.perform(get("/registration")
	            .with(csrf()).with(user("player@mail.com").password("aB123*").roles("PLAYER")))
        	   .andExpect(status().isOk());
        
        mockMvc.perform(get("/registration")
	            .with(csrf()).with(user("admin@mail.com").password("aB123*").roles("ADMIN")))
        	   .andExpect(status().isOk());
    }
    
    @Test
    public void whenSubmitForm_withAllFieldsBlank_thenErrorMessages() throws Exception {
        mockMvc.perform(
	                post("/registration")
	                        .with(csrf())
	                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                        .param("firstName", "")
	                        .param("lastName", "")
	                        .param("email", "")
	                        .param("emailConfirm", "")
	                        .param("password", "")
	                        .param("passwordConfirm", "")
	                        .param("termsAndConditions", "off")
	        )
	        .andExpect(model().hasErrors())
	        .andExpect(model().attributeHasFieldErrors("user", "firstName"))
	        .andExpect(model().attributeHasFieldErrors("user", "lastName"))
	        .andExpect(model().attributeHasFieldErrors("user", "email"))
	        .andExpect(model().attributeHasFieldErrors("user", "emailConfirm"))
	        .andExpect(model().attributeHasFieldErrors("user", "password"))
	        .andExpect(model().attributeHasFieldErrors("user", "passwordConfirm"))
	        .andExpect(model().attributeHasFieldErrors("user", "termsAndConditions"))
	        .andExpect(status().isOk());
    }
    
    @Test
    public void whenSubmitForm_withFirstNameBlank_thenErrorMessages() throws Exception {
        mockMvc.perform(
	                post("/registration")
	                        .with(csrf())
	                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                        .param("firstName", "")
	                        .param("lastName", "DOE")
	                        .param("email", "john.doe@mail.com")
	                        .param("emailConfirm", "john.doe@mail.com")
	                        .param("password", "aB123*")
	                        .param("passwordConfirm", "aB123*")
	                        .param("termsAndConditions", "on")
	        )
	        .andExpect(model().hasErrors())
	        .andExpect(model().attributeHasFieldErrors("user", "firstName"))
	        .andExpect(status().isOk());
    }
    
    @Test
    public void whenSubmitForm_withLastNameBlank_thenErrorMessages() throws Exception {
        mockMvc.perform(
	                post("/registration")
	                        .with(csrf())
	                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                        .param("firstName", "John")
	                        .param("lastName", "")
	                        .param("email", "john.doe@mail.com")
	                        .param("emailConfirm", "john.doe@mail.com")
	                        .param("password", "aB123*")
	                        .param("passwordConfirm", "aB123*")
	                        .param("termsAndConditions", "on")
	        )
	        .andExpect(model().hasErrors())
	        .andExpect(model().attributeHasFieldErrors("user", "lastName"))
	        .andExpect(status().isOk());
    }
    
    @Test
    public void whenSubmitForm_withEmailBlank_thenErrorMessages() throws Exception {
        mockMvc.perform(
	                post("/registration")
	                        .with(csrf())
	                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                        .param("firstName", "John")
	                        .param("lastName", "DOE")
	                        .param("email", "")
	                        .param("emailConfirm", "john.doe@mail.com")
	                        .param("password", "aB123*")
	                        .param("passwordConfirm", "aB123*")
	                        .param("termsAndConditions", "on")
	        )
	        .andExpect(model().hasErrors())
	        .andExpect(model().attributeHasFieldErrors("user", "email"))
	        .andExpect(status().isOk());
    }
    
    @Test
    public void whenSubmitForm_withEmailBadFormat_thenErrorMessages() throws Exception {
        mockMvc.perform(
	                post("/registration")
	                        .with(csrf())
	                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                        .param("firstName", "John")
	                        .param("lastName", "DOE")
	                        .param("email", "bad.email.format")
	                        .param("emailConfirm", "john.doe@mail.com")
	                        .param("password", "aB123*")
	                        .param("passwordConfirm", "aB123*")
	                        .param("termsAndConditions", "on")
	        )
	        .andExpect(model().hasErrors())
	        .andExpect(model().attributeHasFieldErrors("user", "email"))
	        .andExpect(status().isOk());
    }
    
    @Test
    public void whenSubmitForm_withEmailsDontMatch_thenErrorMessages() throws Exception {
        mockMvc.perform(
	                post("/registration")
	                        .with(csrf())
	                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                        .param("firstName", "John")
	                        .param("lastName", "DOE")
	                        .param("email", "john.doe@mail.com")
	                        .param("emailConfirm", "bad.confirmation@mail.com")
	                        .param("password", "aB123*")
	                        .param("passwordConfirm", "aB123*")
	                        .param("termsAndConditions", "on")
	        )
	        .andExpect(model().hasErrors())
	        .andExpect(model().attributeHasFieldErrors("user", "emailConfirm"))
	        .andExpect(status().isOk());
    }
    
    @Test
    public void whenSubmitForm_withPasswordBlank_thenErrorMessages() throws Exception {
        mockMvc.perform(
	                post("/registration")
	                        .with(csrf())
	                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                        .param("firstName", "John")
	                        .param("lastName", "DOE")
	                        .param("email", "john.doe@mail.com")
	                        .param("emailConfirm", "john.doe@mail.com")
	                        .param("password", "")
	                        .param("passwordConfirm", "aB123*")
	                        .param("termsAndConditions", "on")
	        )
	        .andExpect(model().hasErrors())
	        .andExpect(model().attributeHasFieldErrors("user", "password"))
	        .andExpect(status().isOk());
    }
    
    @Test
    public void whenSubmitForm_withPasswordSizeLessThan6Char_thenErrorMessages() throws Exception {
        mockMvc.perform(
	                post("/registration")
	                        .with(csrf())
	                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                        .param("firstName", "John")
	                        .param("lastName", "DOE")
	                        .param("email", "john.doe@mail.com")
	                        .param("emailConfirm", "john.doe@mail.com")
	                        .param("password", "aB1*")
	                        .param("passwordConfirm", "aB1*")
	                        .param("termsAndConditions", "on")
	        )
	        .andExpect(model().hasErrors())
	        .andExpect(model().attributeHasFieldErrors("user", "password"))
	        .andExpect(status().isOk());
    }
    
    @Test
    public void whenSubmitForm_withPasswordSizeMoreThan20Char_thenErrorMessages() throws Exception {
        mockMvc.perform(
	                post("/registration")
	                        .with(csrf())
	                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                        .param("firstName", "John")
	                        .param("lastName", "DOE")
	                        .param("email", "john.doe@mail.com")
	                        .param("emailConfirm", "john.doe@mail.com")
	                        .param("password", "aB1*123456789123456789")
	                        .param("passwordConfirm", "aB1*123456789123456789")
	                        .param("termsAndConditions", "on")
	        )
	        .andExpect(model().hasErrors())
	        .andExpect(model().attributeHasFieldErrors("user", "password"))
	        .andExpect(status().isOk());
    }
    
    @Test
    public void whenSubmitForm_withPasswordNoDigit_thenErrorMessages() throws Exception {
        mockMvc.perform(
	                post("/registration")
	                        .with(csrf())
	                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                        .param("firstName", "John")
	                        .param("lastName", "DOE")
	                        .param("email", "john.doe@mail.com")
	                        .param("emailConfirm", "john.doe@mail.com")
	                        .param("password", "aB*azertyuiop")
	                        .param("passwordConfirm", "aB*azertyuiop")
	                        .param("termsAndConditions", "on")
	        )
	        .andExpect(model().hasErrors())
	        .andExpect(model().attributeHasFieldErrors("user", "password"))
	        .andExpect(status().isOk());
    }
    
    @Test
    public void whenSubmitForm_withPasswordNoLowerCase_thenErrorMessages() throws Exception {
        mockMvc.perform(
	                post("/registration")
	                        .with(csrf())
	                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                        .param("firstName", "John")
	                        .param("lastName", "DOE")
	                        .param("email", "john.doe@mail.com")
	                        .param("emailConfirm", "john.doe@mail.com")
	                        .param("password", "A1234*")
	                        .param("passwordConfirm", "A1234*")
	                        .param("termsAndConditions", "on")
	        )
	        .andExpect(model().hasErrors())
	        .andExpect(model().attributeHasFieldErrors("user", "password"))
	        .andExpect(status().isOk());
    }
    
    @Test
    public void whenSubmitForm_withPasswordNoUpperCase_thenErrorMessages() throws Exception {
        mockMvc.perform(
	                post("/registration")
	                        .with(csrf())
	                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                        .param("firstName", "John")
	                        .param("lastName", "DOE")
	                        .param("email", "john.doe@mail.com")
	                        .param("emailConfirm", "john.doe@mail.com")
	                        .param("password", "a1234*")
	                        .param("passwordConfirm", "a1234*")
	                        .param("termsAndConditions", "on")
	        )
	        .andExpect(model().hasErrors())
	        .andExpect(model().attributeHasFieldErrors("user", "password"))
	        .andExpect(status().isOk());
    }
    
    @Test
    public void whenSubmitForm_withPasswordNoSpecialChar_thenErrorMessages() throws Exception {
        mockMvc.perform(
	                post("/registration")
	                        .with(csrf())
	                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                        .param("firstName", "John")
	                        .param("lastName", "DOE")
	                        .param("email", "john.doe@mail.com")
	                        .param("emailConfirm", "john.doe@mail.com")
	                        .param("password", "aB1234")
	                        .param("passwordConfirm", "aB1234")
	                        .param("termsAndConditions", "on")
	        )
	        .andExpect(model().hasErrors())
	        .andExpect(model().attributeHasFieldErrors("user", "password"))
	        .andExpect(status().isOk());
    }
    
    @Test
    public void whenSubmitForm_withPasswordWithWhitspace_thenErrorMessages() throws Exception {
        mockMvc.perform(
	                post("/registration")
	                        .with(csrf())
	                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                        .param("firstName", "John")
	                        .param("lastName", "DOE")
	                        .param("email", "john.doe@mail.com")
	                        .param("emailConfirm", "john.doe@mail.com")
	                        .param("password", "aB123 *")
	                        .param("passwordConfirm", "aB123 *")
	                        .param("termsAndConditions", "on")
	        )
	        .andExpect(model().hasErrors())
	        .andExpect(model().attributeHasFieldErrors("user", "password"))
	        .andExpect(status().isOk());
    }
    
    @Test
    public void whenSubmitForm_withTermsAndConditionsNotCHecked_thenErrorMessages() throws Exception {
        mockMvc.perform(
	                post("/registration")
	                        .with(csrf())
	                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                        .param("firstName", "John")
	                        .param("lastName", "DOE")
	                        .param("email", "john.doe@mail.com")
	                        .param("emailConfirm", "john.doe@mail.com")
	                        .param("password", "aB123*")
	                        .param("passwordConfirm", "aB123*")
	                        .param("termsAndConditions", "off")
	        )
	        .andExpect(model().hasErrors())
	        .andExpect(model().attributeHasFieldErrors("user", "termsAndConditions"))
	        .andExpect(status().isOk());
    }

    @Test
    public void whenSubmitForm_withEmailAlreadyUsed_thenErrorMessage() throws Exception {
    	//LOG.debug("----->>>> {}", userRepository.findAll().toString());
        mockMvc.perform(
	                post("/registration")
	                        .with(csrf())
	                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                        .param("firstName", "Existing")
	                        .param("lastName", "USER")
	                        .param("email", "existing.user@mail.com")
	                        .param("emailConfirm", "existing.user@mail.com")
	                        .param("password", "aB123*")
	                        .param("passwordConfirm", "aB123*")
	                        .param("termsAndConditions", "off")
	        )
	        .andExpect(model().hasErrors())
	        .andExpect(model().attributeHasFieldErrors("user", "email"))
	        .andExpect(status().isOk());
    }

    @Test
    public void whenSubmitFormAndLogin_withAllFieldsValid_thenAccessGameHomePage() throws Exception {
    	//LOG.debug("----->>>> {}", userRepository.findAll().toString());
      // Create New User
        mockMvc.perform(
	                post("/registration")
	                        .with(csrf())
	                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                        .param("firstName", "John DOE")
	                        .param("lastName", "With Remember Me Token")
	                        .param("email", "john.doe.with.remember.me@mail.com")
	                        .param("emailConfirm", "john.doe.with.remember.me@mail.com")
	                        .param("password", "aB123*")
	                        .param("passwordConfirm", "aB123*")
	                        .param("termsAndConditions", "on")
	        )
        	.andExpect(status().isFound()).andExpect(redirectedUrl("/registration?success"))
        	.andDo(MockMvcResultHandlers.print());
        
	  // Login fresh created user
		mockMvc.perform(
				post("/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", "john.doe.with.remember.me@mail.com")
				.param("password", "aB123*")
				.param("remember-me", "on"))
		
			.andExpect(status().isFound()).andExpect(redirectedUrl("/game/home"));
    }
     
//    Course mockCourse = new Course("1", "Smallest Number", "1",
//			Arrays.asList("1", "2", "3", "4"));
//
//	// studentService.addCourse to respond back with mockCourse
//	Mockito.when(
//			studentService.addCourse(Mockito.anyString(),
//					Mockito.any(Course.class))).thenReturn(mockCourse);
//
//	// Send course as body to /students/Student1/courses
//	RequestBuilder requestBuilder = MockMvcRequestBuilders
//			.post("/students/Student1/courses")
//			.accept(MediaType.APPLICATION_JSON).content(exampleCourseJson)
//			.contentType(MediaType.APPLICATION_JSON);
//
//	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//	MockHttpServletResponse response = result.getResponse();
//
//	assertEquals(HttpStatus.CREATED.value(), response.getStatus());
//
//	assertEquals("http://localhost/students/Student1/courses/1",
//			response.getHeader(HttpHeaders.LOCATION));
    
	@After
	public void tearDown() throws Exception {
	  //####  run after each test
		
	}
    
}