package com.ca.lextest.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ca.lextest.LexiconTestApplication;
import com.ca.lextest.config.MessagesConfig;
import com.ca.lextest.db.config.MySqlConfig;
import com.ca.lextest.db.model.User;
import com.ca.lextest.service.UserService;
import com.ca.lextest.web.model.UserRegistrationDto;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

	private static final Logger LOG = LoggerFactory.getLogger(LexiconTestApplication.class);	
	@Autowired
    private MySqlConfig mySqlConfig;	
    @Autowired
    private UserService userService;
	@Autowired
    private MessagesConfig messages;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user")
    								  @Valid UserRegistrationDto userDto, BindingResult result){
    	
        LOG.debug("----->>>> [UserRegistrationController] registerUserAccount");
		
        User existing = userService.findByEmail(userDto.getEmail());
        if (existing != null){
            LOG.debug("----->>>> [UserRegistrationController] Error : existing E-mail");
            result.rejectValue("email", "error.email", messages.get("Duplicate.user.email"));
        }

        if (result.hasErrors()){
            LOG.debug("----->>>> [UserRegistrationController] registerUserAccount :  hasErrors !! : |{}|", result.getAllErrors().toString());
            return "registration";
        }

        LOG.debug("----->>>> [UserRegistrationController] registerUserAccount :  userService.save(userDto) : |{}|", userDto.toString());
        userService.save(userDto);
        LOG.debug("----->>>> [UserRegistrationController] registerUserAccount :  Success");
                return "redirect:/registration?success";
    }

}
