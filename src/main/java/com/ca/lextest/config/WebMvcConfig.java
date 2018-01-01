package com.ca.lextest.config;

import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.ca.lextest.LexiconTestApplication;
import com.ca.lextest.config.MessagesConfig;

//@EnableWebMvc
@Configuration
//@ComponentScan("com.ca.lextest.web")
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	private static final Logger LOG = LoggerFactory.getLogger(LexiconTestApplication.class);	
	@Autowired
    private MessageSource messageSource;	
	@Autowired
    private MessagesConfig messages;
		
	/*=================================
	  Internationalization i18n 
	  =================================*/	
	@Bean
	public LocaleResolver localeResolver() {
	    SessionLocaleResolver slr = new SessionLocaleResolver();
	    // TODO à tester...	    
	    slr.setDefaultLocale(Locale.ENGLISH);	    
	    for(String lang: messages.getLanguages()) {
	    	if(lang == LocaleContextHolder.getLocale().getLanguage()) {
	    		slr.setDefaultLocale(LocaleContextHolder.getLocale());
	    	}
	    }
	    return slr;   
	}
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
	    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
	    //GET param retrieved from URL "http://...?lang=en"
	    lci.setParamName("lang");
	    return lci;
	}
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
	
	/*=================================
	  Spring Data JPA Validation
	  =================================*/
	/* Set the validation messages location to the global one
	 * messages source (messages_xx.proporties), rather than  (ValidationMessages.properties) 
	 * used by default for validation messages
	 */
	@Bean(name = "validator")
	public LocalValidatorFactoryBean validator()
	{
	    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
	    bean.setValidationMessageSource(messageSource);
	    return bean;
	}	 
	@Override
	public Validator getValidator()
	{
	    return validator();
	}
	
	/*=================================
	  Auto Mapping DTO<->Entity/Model attributes
	  =================================*/
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
	
}