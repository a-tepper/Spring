package org.example.controller;

import org.example.dto.LoginForm;
import org.example.exceptions.BookShelfLoginException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public interface LoginController {

    @GetMapping
    String login(Model model);

    @GetMapping("/new_user")
    String newUser(Model model);

    @PostMapping("/auth")
    String authenticate(LoginForm loginForm) throws BookShelfLoginException;

    @PostMapping("/sign_up")
    String signUp(LoginForm loginForm);
}
