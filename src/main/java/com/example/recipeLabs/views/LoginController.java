package com.example.recipeLabs.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public String login() {
        return "login"; // login.jsp 또는 login.html로 리턴
    }
}