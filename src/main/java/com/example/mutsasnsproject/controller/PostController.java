package com.example.mutsasnsproject.controller;


import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/v1/posts")
public class PostController {
    private final PostService postService;
    @GetMapping("/list")
    public String list(@PageableDefault(size = 20, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable, Model model){
        Page<PostDetailResponse> page = postService.list(pageable);
        model.addAttribute("posts",page);
        return "posts/list";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long id){
        if(id == null){
            model.addAttribute("postRequest",new PostRequest());
        }else{
            PostDetailResponse postDetailResponse = postService.get("admin",id);
            PostRequest postRequest = PostRequest.builder()
                    .body(postDetailResponse.getBody())
                    .title(postDetailResponse.getTitle())
                    .build();
            model.addAttribute("postRequest",postRequest);
        }
        return "posts/form";
    }

    @PostMapping("/form")
    public String form(@ModelAttribute PostRequest postRequest){
        try {
            postService.add("admin", postRequest.getBody(), postRequest.getTitle());
        }catch (AppException e){
            System.out.println(e.getErrorCode().getMessage() +  "에러");
        }
        return "redirect:/view/v1/posts/list";
    }
}
