package com.example.mutsasnsproject.restcontroller;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.service.GoodService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class GoodRestController {
    private final GoodService goodService;
    //    좋아요기능 ----------------------------------------------------

    @ApiOperation(value = "좋아요 실행/취소기능")
    @PostMapping("/{postId}/likes")
    public Response<String> goodPost(@PathVariable Long postId, Authentication authentication) {
            String message = goodService.postGood(postId, authentication.getName());
            return Response.success(message);
    }

    @ApiOperation(value = "좋아요 조회기능")
    @GetMapping("/{postId}/likes")
    public Response<Integer> goodCount(@PathVariable long postId) {
        int count = goodService.countGood(postId);
        return Response.success(count);
    }
}
