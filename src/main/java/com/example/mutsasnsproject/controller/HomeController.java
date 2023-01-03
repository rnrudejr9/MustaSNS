package com.example.mutsasnsproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/v1/")
public class HomeController {
    @GetMapping("/home")
    public String index(Model model){
        model.addAttribute("home","hello");
        return "home";
    }
}
