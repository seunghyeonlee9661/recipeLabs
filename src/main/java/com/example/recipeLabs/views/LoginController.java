package com.example.recipeLabs.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Oauth2 기능 테스트를 위해 만든 임시 View
@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public String login() {
        return "login"; // login.jsp 또는 login.html로 리턴
    }
}