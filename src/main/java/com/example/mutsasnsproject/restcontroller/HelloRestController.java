package com.example.mutsasnsproject.restcontroller;

import com.example.mutsasnsproject.service.AlgorithmService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.jsse.JSSEKeyManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("api/v1/")
@Slf4j
@RequiredArgsConstructor
public class HelloRestController {
    private final AlgorithmService algorithmService;
    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok().body("구경덕");
    }

    @ApiOperation(value = "code Test")
    @GetMapping("/hello/{num}")
    public ResponseEntity<Integer> calc(@PathVariable int num){
        return ResponseEntity.ok().body(algorithmService.sumOfDigit(num));
    }



}
