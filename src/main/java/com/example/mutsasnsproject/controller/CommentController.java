package com.example.mutsasnsproject.controller;

import com.example.mutsasnsproject.domain.dto.comment.CommentRequest;
import com.example.mutsasnsproject.domain.dto.comment.CommentResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/v1/posts")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public String makeComment(@PathVariable("postId") Long postId, @Valid CommentRequest commentRequest, Authentication authentication){
        commentService.commentAdd(authentication.getName(),postId,commentRequest);

        return "redirect:/view/v1/posts/detail/" + postId;
    }

    @PostMapping("/{postId}/comments/delete/{id}")
    public String deleteComment(@PathVariable("postId") Long postId, Authentication authentication,@PathVariable Long id){
        commentService.commentDelete(authentication.getName(),postId,id);
        return "redirect:/view/v1/posts/detail/" + postId;
    }

    @GetMapping("/{postId}/comments/modify/{id}")
    public String modifyComment(@PathVariable("postId") Long postId, Authentication authentication, @PathVariable Long id, Model model){
        CommentResponse commentResponse = commentService.findById(id);
        model.addAttribute("commentRequest", new CommentRequest(commentResponse.getComment()));
        model.addAttribute("postId",postId);
        model.addAttribute("id",id);
        return "comments/modify";
    }

    @PostMapping("/{postId}/comments/modify/{id}")
    public String modifyComment(@PathVariable("postId") Long postId, Authentication authentication, @PathVariable Long id, @Valid CommentRequest commentRequest){
        commentService.commentModify(authentication.getName(),postId,commentRequest,id);
        return "redirect:/view/v1/posts/detail/" + postId;
    }


}
