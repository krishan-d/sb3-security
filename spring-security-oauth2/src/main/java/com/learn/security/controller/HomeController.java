package com.learn.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    // localhost:8082/
    @GetMapping
    public String home() {
        return "This is spring security, oauth2 demo!!!";
    }
}
