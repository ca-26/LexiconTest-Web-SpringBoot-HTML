package com.ca.lextest.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.ca.lextest.LexiconTestApplication;

@Controller
public class MainController {

	private static final Logger LOG = LoggerFactory.getLogger(LexiconTestApplication.class);
	
    @GetMapping("/")
    public String root(Model model) {
        LOG.debug("----->>>> [MainController] -> root path -> login");
        //model.addAttribute("msg","a jar packaging example");
        return "login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        LOG.debug("----->>>> [MainController] -> login");
        return "login";
    }

    @GetMapping("/game/home")
    public String gameHome(Model model) {
        LOG.debug("----->>>> [MainController] -> game/home");
        return "game/home";
    }
    
    @GetMapping("/admin/home")
    public String adminHome(Model model) {
        LOG.debug("----->>>> [MainController] -> admin/home");
        return "admin/home";
    }    

    @GetMapping("/accessdenied")
    @PostMapping("/accessdenied")
    public String accessDeniedGet(Model model) {
        LOG.debug("----->>>> [MainController] -> accessdenied");
        return "accessdenied";
    }
    
    @GetMapping("/error")
    @PostMapping("/error")
    public String errorGet(Model model) {
        LOG.debug("----->>>> [MainController] -> error");
        return "error";
    }

}
