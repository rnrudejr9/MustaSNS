package com.example.mutsasnsproject.controller;


import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/v1/posts")
public class PostController {
    private final PostService postService;

    // 리스트 전체 출력 --------------------------

    @GetMapping("/list")
    public String list(Model model,@PageableDefault(size = 10, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(required = false,defaultValue = "") String searchText){
//        Page<PostDetailResponse> page = postService.list(pageable);

        Page<PostDetailResponse> page = postService.findList(pageable,searchText,searchText);

        int startPage = Math.max(1,page.getPageable().getPageNumber() - 4);
        int endPage = Math.min(page.getTotalPages(),page.getPageable().getPageNumber() + 4);
        model.addAttribute("posts",page);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        return "posts/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id){
        PostDetailResponse postDetailResponse = postService.get("admin",id);
        return "";
    }


    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long id){
        if(id == null){
            model.addAttribute("postRequest",new PostRequest());
        }else{
            PostDetailResponse postDetailResponse = postService.get(postService.findById(id).getUser().getUserName(),id);
            PostRequest postRequest = PostRequest.builder()
                    .body(postDetailResponse.getBody())
                    .title(postDetailResponse.getTitle())
                    .build();
            model.addAttribute("postRequest",postRequest);
        }
        return "posts/form";
    }

    @PostMapping("/form")
    public String form(@Valid PostRequest postRequest, BindingResult bindingResult,Authentication authentication){
        if(bindingResult.hasErrors()){
            return "posts/form";
        }

        postService.add(authentication.getName(), postRequest.getBody(), postRequest.getTitle());
        return "redirect:/view/v1/posts/list";
    }

}
