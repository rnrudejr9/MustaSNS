package com.example.mutsasnsproject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("api/v1/")
@Slf4j
public class HelloController {
    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok().body("구경덕");
    }

    @GetMapping("/hello/{num}")
    public ResponseEntity<Integer> calc(@PathVariable int num){
        int res = 0;
        System.out.println(num);
        while(num / 10 != 0){
            res += num % 10;
            num = num/10;
        }
        res += num % 10;
        System.out.println(res);
        return ResponseEntity.ok().body(res);
    }
}
