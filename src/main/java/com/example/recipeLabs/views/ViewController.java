package com.example.recipeLabs.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users/login")
public class ViewController {
    @GetMapping
    public String login(){
        return "login";
    }
}
