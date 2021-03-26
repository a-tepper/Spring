package org.example.controller;

import org.apache.log4j.Logger;
import org.example.service.LoginService;
import org.example.dto.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/login")
public class LoginControllerImpl implements LoginController {

    private final Logger logger = Logger.getLogger(LoginControllerImpl.class);
    private final LoginService loginService;

    @Autowired
    public LoginControllerImpl(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public String login(Model model) {
        logger.info("GET /login returns login_page.html");
        model.addAttribute("loginForm", new LoginForm());
        return "login_page";
    }

    @Override
    public String newUser(Model model) {
        logger.info("GET /login returns login_page.html");
        model.addAttribute("loginForm", new LoginForm());
        return "new_user";
    }

    @Override
    public String authenticate(LoginForm loginForm) {
        if (loginService.authenticate(loginForm)) {
            logger.info("login OK redirect to book shelf");
            return "redirect:/books/shelf";
        } else {
            logger.info("login FAIL redirect back to login");
            return "redirect:/login";
        }
    }

    @Override
    public String signUp(LoginForm loginForm) {
        if (loginForm.getUsername() == "" || loginForm.getPassword() == ""){
            logger.info("empty username or password! redirect back to new_user");
            return "redirect:/login/new_user";
        }
        else {
            if (loginService.signUp(loginForm)) {
                logger.info("sign-up OK redirect to login");
                return "redirect:/login";
            } else {
                logger.info("sign-up FAIL redirect back to new_user");
                return "redirect:/login/new_user";
            }
        }
    }
}
