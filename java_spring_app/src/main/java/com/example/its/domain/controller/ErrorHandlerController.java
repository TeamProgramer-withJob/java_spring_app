package com.example.its.domain.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/error")
@RequiredArgsConstructor
public class ErrorHandlerController {
	
    @GetMapping()
    public String errorDefault() {
        return "error/error";
    }

    @GetMapping("/403")
    public String error403() {
        return "error/403";
    }
}
