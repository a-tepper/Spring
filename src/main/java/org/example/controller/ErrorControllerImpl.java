package org.example.controller;

import org.springframework.stereotype.Controller;

@Controller
public class ErrorControllerImpl implements ErrorController{
    @Override
    public String notFoundError(){
        return "errors/404";
    }
}
