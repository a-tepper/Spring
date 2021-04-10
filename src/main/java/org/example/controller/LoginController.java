package org.example.controller;

import org.example.dto.LoginForm;
import org.example.exceptions.BookShelfLoginException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

public interface LoginController {

    @GetMapping
    String login(Model model);

    @GetMapping("/new_user")
    String newUser(Model model);

    @PostMapping("/auth")
    String authenticate(@Valid LoginForm loginForm, BindingResult bindingResult, Model model) throws BookShelfLoginException;

    @PostMapping("/sign_up")
    String signUp(LoginForm loginForm);
}
