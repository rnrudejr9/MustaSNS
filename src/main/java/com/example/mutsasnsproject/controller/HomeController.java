package com.example.mutsasnsproject.controller;

import com.example.mutsasnsproject.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/v1/")
public class HomeController {
    @GetMapping("/home")
    public String index(Model model){
        try {
            model.addAttribute("home", "hello");
            return "home";
        }catch (AppException e){
            model.addAttribute("error",e.getErrorCode().getMessage());
            return "error";
        }
    }


    @GetMapping("/start")
    public String hello(){
        return "test";
    }

    @PostMapping("/ajax")
    public String getMembers(Model model) throws Exception { // /get/member로 호출 시 가상의 memberList를 작성하여 html에 전달

        Map<Integer, String> memberList = new HashMap<>(); // <회원번호, 회원이름>으로 구성된 가상의 멤버 리스트
        memberList.put(1, "킴오리");
        memberList.put(10, "킴엔탈");
        memberList.put(20, "리오리");
        memberList.put(200, "리엔탈");

        model.addAttribute("memberList", memberList);
        return "ajaxtest :: memberTable"; // template 파일 이름 + '::' + 데이터가 변경 될 fragment id

    }
}
