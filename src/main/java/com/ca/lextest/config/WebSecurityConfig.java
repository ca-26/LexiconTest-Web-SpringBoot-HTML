package com.ca.lextest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.ca.lextest.LexiconTestApplication;
import com.ca.lextest.db.config.MySqlConfig;
import com.ca.lextest.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger LOG = LoggerFactory.getLogger(LexiconTestApplication.class);
	@Autowired
	MySqlConfig mySqlConfig;	
	@Autowired
	private UserService userService;
	@Autowired
	private UserDetailsService userDetailsService;
		
	@Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
	
	/* Persistent Token Remember-Me */
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(mySqlConfig.mysqlDataSource());
        return tokenRepository;
    }
	
	/* To use Spring Security Tags */
	@Bean
	public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver, SpringSecurityDialect sec) {
	    final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	    templateEngine.setTemplateResolver(templateResolver);
	    templateEngine.addDialect(sec); // Enable use of "sec" tags
	    return templateEngine;
	}
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {

		LOG.info("==================[Spring Security configuration]==================");
        http
	        .authorizeRequests()
				.antMatchers(
	                         "/",
	                         "/home",
	                         "/login",
	                         "/registration").permitAll()
				.antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
				.antMatchers("/game/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PLAYER")
				.anyRequest().authenticated()
            .and()
                .formLogin()
                    .loginPage("/login").failureUrl("/login?error=true")
    				.defaultSuccessUrl("/game/home")
    				.usernameParameter("email")
    				.passwordParameter("password")
            .and()
                .logout()
                	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                	//.logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
    				//.logoutSuccessHandler(logoutSuccessHandler)
    				//.addLogoutHandler(logoutHandler)
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
    				//.deleteCookies(cookieNamesToClear)
            .and().exceptionHandling().accessDeniedPage("/accessdenied")
            
            /*Persistent Token Remember-Me*/
            .and()
				.rememberMe()
					.rememberMeCookieName("lexicontest-remember-me")
					.tokenValiditySeconds(24 * 60 * 60) // expired time = 1 day
					.tokenRepository(persistentTokenRepository())
					// To by-pass the Spring Boot Bug below - Remember me doesn't work:
					//https://stackoverflow.com/questions/46421185/remember-me-not-working-throws-java-lang-illegalstateexception-userdetailsse
					.and().rememberMe().key("uniqueAndSecret").userDetailsService(userDetailsService);
    }
    
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web
	       .ignoring()
	       .antMatchers("/resources/**", "/static/**", "/lib/**", "/css/**", "/js/**", "/images/**", "/webjars/**");
	}
    
	

	
/*
	.antMatchers("/api/auction").hasRole("USER")
	.antMatchers("/ws").hasRole("USER")
	.antMatchers("/api/oldbids").hasRole("USER")
	.antMatchers("/oldbids").hasRole("USER")
	.antMatchers("/websockets.html").hasRole("USER")
	.antMatchers("/api/user").hasAnyRole("ANONYMOUS", "USER");
	       

	private String usersQuery;
	
	@Value("${spring.queries.roles-query}")
	private String rolesQuery;

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.
			jdbcAuthentication()
				.usersByUsernameQuery(usersQuery)
				.authoritiesByUsernameQuery(rolesQuery)
				.dataSource(dataSource)
				.passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.
			authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/registration").permitAll()
				.antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest()
				.authenticated()
				
			.and().csrf().disable().formLogin()
				.loginPage("/login").failureUrl("/login?error=true")
				.defaultSuccessUrl("/admin/home")
				.usernameParameter("email")
				.passwordParameter("password")
				
			.and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				//.logoutUrl("/logout")
				.logoutSuccessUrl("/login")
				//.logoutSuccessHandler(logoutSuccessHandler) 
				.invalidateHttpSession(true)
				//.addLogoutHandler(logoutHandler)
				//.deleteCookies(cookieNamesToClear)
				//.and()
			.and().exceptionHandling().accessDeniedPage("/accessdenied");
*/		
		/*
		.authorizeRequests()
        .antMatchers("/", "/home").permitAll()
        .anyRequest().authenticated()
        .and()
    .formLogin()
        .loginPage("/login")
        .permitAll()
        .and()
    .logout()
        .permitAll();
		*/
		/*
		 	.antMatchers("/api/auction").hasRole("USER")
            .antMatchers("/ws").hasRole("USER")
            .antMatchers("/api/oldbids").hasRole("USER")
            .antMatchers("/oldbids").hasRole("USER")
            .antMatchers("/websockets.html").hasRole("USER")
            .antMatchers("/api/user").hasAnyRole("ANONYMOUS", "USER");
            */
		 
/*			
	}
*/	

	
	
	/*
	// In Memory Auth Example
	@Configuration
	@EnableWebSecurity
	public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	            .authorizeRequests()
	                .antMatchers("/", "/home").permitAll()
	                .anyRequest().authenticated()
	                .and()
	            .formLogin()
	                .loginPage("/login")
	                .permitAll()
	                .and()
	            .logout()
	                .permitAll();
	    }

	    @Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	        auth
	            .inMemoryAuthentication()
	                .withUser("user").password("password").roles("USER");
	    }
	*/
	


}