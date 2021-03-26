package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;

public interface ErrorController {
    @GetMapping("/404")
    String notFoundError();
}
