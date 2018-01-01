package com.ca.lextest.config;

import java.util.ArrayList;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import com.ca.lextest.LexiconTestApplication;


/**
 * Bean given access to i18n (internationnalization) messages from Java code.
 * 
 * This finds messages automatically found from src/main/resources (files named messages_*.properties)
 * 
 * This uses automatically the client locale (language).
 *
 * @author Cihan AKKOYUN
 * @version 1.0
 * @since 2017-12-20
 */
@Component
public class MessagesConfig {

	private static final Logger LOG = LoggerFactory.getLogger(LexiconTestApplication.class);	
	/**
     * Global messages source
     */
	@Autowired
    private MessageSource messageSource;
	private MessageSourceAccessor accessor;
	
	private Locale trLocale= new Locale("tr", "TR"); //Language, Country	
	private ArrayList<String> languages = new ArrayList<String>();
	{
		languages = new ArrayList<String>();
		languages.add(Locale.ENGLISH.getLanguage());
		languages.add(Locale.FRENCH.getLanguage());
		languages.add(trLocale.getLanguage());
	}
	
	/**
     * Initializes the accessor pointing to the global messages source
     */
    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, LocaleContextHolder.getLocale());
		LOG.info("==================[MessageSourceAccessor(i18n) configured]==================");
    }
    
    /**
     * Update the accessor language pointing to the global messages source
     * if the user change the language
     */
    public void updateLanguage() {
    	init();
    }
    
    /**
     * Get the content of a message corresponding to the client current locale(language).
     * 
     * @param msgId
     *            Id of the message to retrieve.
     * @return The content of the requested message.
     */
    public String get(String msgId) {
        //return accessor.getMessage(msgId);
        return accessor.getMessage(msgId, LocaleContextHolder.getLocale());
    }

    /**
     * Return the list of all available languages.
     * 
     * @return List of languages.
     */
	public ArrayList<String> getLanguages() {
		return languages;
	}
    
    
}