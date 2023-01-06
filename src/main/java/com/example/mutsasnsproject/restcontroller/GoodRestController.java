package com.example.mutsasnsproject.restcontroller;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.entity.Good;
import com.example.mutsasnsproject.service.GoodSerivce;
import com.example.mutsasnsproject.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class GoodRestController {
    private GoodSerivce goodSerivce;
    //    좋아요기능 ----------------------------------------------------

    @ApiOperation(value = "좋아요 실행/취소기능")
    @PostMapping("/{postId}/likes")
    public Response<String> goodPost(@PathVariable Long postId, Authentication authentication) {
        String userName = authentication.getName();
        String message = goodSerivce.postGood(postId, userName);
        return Response.success(message);
    }

    @ApiOperation(value = "좋아요 조회기능")
    @GetMapping("/{postId}/likes")
    public Response<Integer> goodCount(@PathVariable long postId) {
        int count = goodSerivce.countGood(postId);
        return Response.success(count);
    }
}
