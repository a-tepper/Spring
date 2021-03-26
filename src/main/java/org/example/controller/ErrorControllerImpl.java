package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Controller //fixme (???): with @ControllerAdvice it doesn't work for not existing urls
@ControllerAdvice
public class ErrorControllerImpl implements ErrorController{
    @Override
    public String notFoundError(){
        return "errors/404";
    }
}
