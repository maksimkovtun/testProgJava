package com.program.testProgJava.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HTMLController {
    @Autowired
    public HTMLController(){}

    @GetMapping("/")
    public String showHomePage(){
        return "redirect:/index.html";
    }
}
