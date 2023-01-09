package com.example.mutsasnsproject.controller;

import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.service.GoodService;
import com.example.mutsasnsproject.service.NotificationService;
import com.example.mutsasnsproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/v1/posts")
public class GoodController {

    private final GoodService goodService;
    @PostMapping("/good/{postId}")
    public String goodPost(Authentication authentication, @PathVariable Long postId, Model model){
        String message = goodService.postGood(postId, authentication.getName());
        model.addAttribute("goodMessages",message);
        return "redirect:/view/v1/posts/detail/" + postId;
    }


}
