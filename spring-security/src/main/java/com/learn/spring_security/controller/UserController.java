package com.learn.spring_security.controller;

import com.learn.spring_security.model.User;
import com.learn.spring_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        userService.register(user);
        return user;
    }
    /*{
    "id":3,
    "name":"edwin",
    "password":"ed@123"
    }*/

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userService.verify(user);
    }


    @GetMapping("/users")
    public ResponseEntity<List<User>> getAll() {
        List<User> list = userService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
