package com.example.mutsasnsproject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/")
@Slf4j
public class UserController {
    @GetMapping("/hello")
    public String hello(){
        log.info("hello");
        return "main";
    }

}
