package com.ca.lextest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//If main class not in the root package :
//	@Configuration
//	@EnableAutoConfiguration
//	@ComponentScan
//(@SpringBootApplication already includes this 3 annotations)
//@EnableJpaRepositories(basePackages = "com.ca.lextest")
//@EntityScan(basePackages = "com.ca.lextest")
@SpringBootApplication

//Application types : 
//	Web		"no extends"
//	JSP :	extends SpringBootServletInitializer
//	MVC :	extends WebMvcConfigurerAdapter
public class LexiconTestApplication extends WebMvcConfigurerAdapter {
	
	private static final Logger LOG = LoggerFactory.getLogger(LexiconTestApplication.class);
	
	/*
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LexiconTestApplication.class);
    }
	*/
	
	/*
    private static TemplateEngine templateEngine;    
    static {
        initializeTemplateEngine();
    }  
    private static void initializeTemplateEngine() {
        
        ServletContextTemplateResolver templateResolver = 
            new ServletContextTemplateResolver();
        // XHTML is the default mode, but we set it anyway for better understanding of code
        templateResolver.setTemplateMode("XHTML");
        // This will convert "home" to "/WEB-INF/templates/home.html"
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        // Template cache TTL=1h. If not set, entries would be cached until expelled by LRU
        templateResolver.setCacheTTLMs(3600000L);
        
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);        
    }
    */

	public static void main(String[] args) throws Throwable {
		SpringApplication.run(LexiconTestApplication.class, args);		
		LOG.info("==================[LexiconTestApplication application started]==================");
	}

}
