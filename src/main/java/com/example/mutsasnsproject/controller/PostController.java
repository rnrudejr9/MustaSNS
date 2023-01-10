package com.example.mutsasnsproject.controller;


import com.example.mutsasnsproject.domain.dto.comment.CommentListResponse;
import com.example.mutsasnsproject.domain.dto.comment.CommentRequest;
import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.service.CommentService;
import com.example.mutsasnsproject.service.GoodService;
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
    private final CommentService commentService;
    private final GoodService goodService;

    // 리스트 전체 출력 --------------------------

    @GetMapping("/list")
    public String list(Model model,@PageableDefault(size = 10, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(required = false,defaultValue = "") String searchText, Authentication authentication){
//        Page<PostDetailResponse> page = postService.list(pageable);
            Page<PostDetailResponse> page = postService.findList(pageable, searchText, searchText);
            int nowPage = page.getPageable().getPageNumber() + 1;
            int startPage = Math.max(nowPage - 4, 1);
            int endPage = Math.min(page.getTotalPages(), nowPage + 4);
            model.addAttribute("posts", page);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            return "posts/list";

    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Long id,Authentication authentication,@PageableDefault(size = 5, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable){
        try {
            if (authentication.getName().equals("anonymousUser")) {
                PostDetailResponse postDetailResponse = postService.get(authentication.getName(), id);
                model.addAttribute("postDetailResponse", postDetailResponse);
                Page<CommentListResponse> page = commentService.commentList(id, pageable);
                int nowPage = page.getPageable().getPageNumber() + 1;
                int startPage = Math.max(nowPage - 4, 1);
                int endPage = Math.min(page.getTotalPages(), nowPage + 4);

                model.addAttribute("commentRequest", new CommentRequest());
                model.addAttribute("comments", page);
                model.addAttribute("startPage", startPage);
                model.addAttribute("endPage", endPage);
                return "posts/detail";
            }

            try {
                PostDetailResponse postDetailResponse = postService.get(authentication.getName(), id);
                model.addAttribute("postDetailResponse", postDetailResponse);
                User user = userService.loadUserByUsername(authentication.getName());

                Page<CommentListResponse> page = commentService.commentList(id, pageable);
                int nowPage = page.getPageable().getPageNumber() + 1;
                int startPage = Math.max(nowPage - 4, 1);
                int endPage = Math.min(page.getTotalPages(), nowPage + 4);

                boolean isContainGood = goodService.isContainGood(user, postService.findById(id));
                if (isContainGood) {
                    model.addAttribute("goodCheck", 1);
                } else {
                    model.addAttribute("goodCheck", 0);
                }


                model.addAttribute("user", user);
                model.addAttribute("postId", id);
                model.addAttribute("commentRequest", new CommentRequest());
                model.addAttribute("comments", page);
                model.addAttribute("startPage", startPage);
                model.addAttribute("endPage", endPage);
                return "posts/detail";
            } catch (Exception e) {
                return "error";
            }
        }catch (NullPointerException e){
            PostDetailResponse postDetailResponse = postService.get(id);
            model.addAttribute("postDetailResponse", postDetailResponse);
            Page<CommentListResponse> page = commentService.commentList(id, pageable);
            int nowPage = page.getPageable().getPageNumber() + 1;
            int startPage = Math.max(nowPage - 4, 1);
            int endPage = Math.min(page.getTotalPages(), nowPage + 4);

            model.addAttribute("postId",postDetailResponse.getId());
            model.addAttribute("commentRequest", new CommentRequest());
            model.addAttribute("comments", page);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            return "posts/detail";
        }
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
            if (bindingResult.hasErrors()) {
                return "posts/form";
            }
            postService.add(authentication.getName(), postRequest);
            return "redirect:/view/v1/posts/list";
    }



//    글 삭제 기능 ------------------------------------------------

    @PostMapping("/delete/{id}")
    public String delete(Authentication authentication, @PathVariable(value="id") Long id){
            postService.delete(authentication.getName(), id);
            return "redirect:/view/v1/posts/list";
    }

    @PostMapping("/modify/{id}")
    public String modify(Authentication authentication, @PathVariable(value="id") Long id, @Valid Post post){
            PostRequest postRequest = PostRequest.builder().body(post.getBody()).title(post.getTitle()).build();
            postService.modify(authentication.getName(), id, postRequest);
            return "redirect:/view/v1/posts/list";
    }

//    마이페이지 기능 --------------------------------------------
    @GetMapping("/my")
    public String myPage(Authentication authentication, Model model, @PageableDefault(size = 5, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable){
            Page<PostDetailResponse> page = postService.myPages(authentication.getName(), pageable);
            int nowPage = page.getPageable().getPageNumber() + 1;
            int startPage = Math.max(nowPage - 4, 1);
            int endPage = Math.min(page.getTotalPages(), nowPage + 4);
            model.addAttribute("posts", page);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            return "posts/my";
    }

}
