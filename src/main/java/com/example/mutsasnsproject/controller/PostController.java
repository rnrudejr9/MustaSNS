package com.example.mutsasnsproject.controller;


import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.service.PostService;
import com.example.mutsasnsproject.service.UserService;
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
    private final UserService userService;

    // 리스트 전체 출력 --------------------------

    @GetMapping("/list")
    public String list(Model model,@PageableDefault(size = 10, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(required = false,defaultValue = "") String searchText, Authentication authentication){
//        Page<PostDetailResponse> page = postService.list(pageable);

        Page<PostDetailResponse> page = postService.findList(pageable,searchText,searchText);

        int startPage = Math.max(1,page.getPageable().getPageNumber() - 4);
        int endPage = Math.min(page.getTotalPages(),page.getPageable().getPageNumber() + 4);
        model.addAttribute("posts",page);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        return "posts/list";
    }




    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Long id,Authentication authentication){
        PostDetailResponse postDetailResponse = postService.get(authentication.getName(),id);
        model.addAttribute("postDetailResponse",postDetailResponse);
        User user = userService.loadUserByUsername(authentication.getName());
        model.addAttribute("user",user);
        model.addAttribute("postId",id);
        return "posts/detail";
    }

//글 작성 기능 ----------------------------------------------------

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long id){
        if(id == null){
            model.addAttribute("postRequest",new PostRequest());
        }else{
            Post post = postService.findById(id);
            model.addAttribute("post",post);
            return "posts/modify";
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

//    글 삭제 기능 ------------------------------------------------

    @PostMapping("/delete/{id}")
    public String delete(Authentication authentication, @PathVariable(value="id") Long id){
        postService.delete(authentication.getName(),id);
        return "redirect:/view/v1/posts/list";
    }

    @PostMapping("/modify/{id}")
    public String modify(Authentication authentication, @PathVariable(value="id") Long id, @Valid Post post){
        PostRequest postRequest = PostRequest.builder().body(post.getBody()).title(post.getTitle()).build();
        postService.modify(authentication.getName(),id,postRequest);
        return "redirect:/view/v1/posts/list";
    }



}
