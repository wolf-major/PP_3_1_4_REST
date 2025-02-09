package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



import jakarta.validation.Valid;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;


@Controller
@Valid
public class UsersController {
    private final UserService userService;

    @Autowired
    public UsersController(UserService userServiceInt) {
        this.userService = userServiceInt;
    }

    @GetMapping(value = "/user")
    @PreAuthorize("hasAuthority('USER')")
    public String getUserPage() {
        return "user_page";
    }
}

