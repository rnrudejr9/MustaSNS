package com.example.mutsasnsproject.restcontroller;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.comment.CommentListResponse;
import com.example.mutsasnsproject.domain.dto.comment.CommentRequest;
import com.example.mutsasnsproject.domain.dto.comment.CommentResponse;
import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.domain.dto.post.PostResponse;
import com.example.mutsasnsproject.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostRestController {
    private final PostService postService;

//      게시글 crud ----------------------------------------------------
    @ApiOperation(value = "게시글 작성기능", notes = "로그인 한 사용자가 title,body 값으로 게시글 작성")
    @PostMapping
    public Response<PostResponse> addPost(@ApiIgnore Authentication authentication, @RequestBody PostRequest postRequest) {
        String userName = authentication.getName();
        PostResponse postResponse = postService.add(userName, postRequest.getBody(), postRequest.getTitle());
        return Response.success(postResponse);
    }

    @ApiOperation(value = "게시글 수정기능", notes = "로그인 한 사용자가 title,body 값으로 게시글 수정")
    @PutMapping("/{id}")
    public Response<PostResponse> modifyPost(@ApiIgnore Authentication authentication, @RequestBody PostRequest postRequest, @PathVariable Long id) {
        String userName = authentication.getName();
        PostResponse postResponse = postService.modify(userName, id, postRequest);
        return Response.success(postResponse);
    }

    @ApiOperation(value = "게시글 삭제기능")
    @DeleteMapping("/{id}")
    public Response<PostResponse> deletePost(@ApiIgnore Authentication authentication, @PathVariable Long id) {
        String userName = authentication.getName();
        PostResponse postResponse = postService.delete(userName, id);
        return Response.success(postResponse);
    }

    @ApiOperation(value = "게시글 상세조회기능")
    @GetMapping("/{id}")
    public Response<PostDetailResponse> getPost(@ApiIgnore Authentication authentication, @PathVariable Long id) {
        String userName = authentication.getName();
        PostDetailResponse postDetailResponse = postService.get(userName, id);
        return Response.success(postDetailResponse);
    }

    @ApiOperation(value = "게시글 전체조회기능")
    @GetMapping
    public Response<Page<PostDetailResponse>> listPost(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDetailResponse> postListResponses = postService.list(pageable);
        return Response.success(postListResponses);
    }


//    마이 피드 기능 ------------------------------------------------------

    @ApiOperation(value = "마이피드 기능")
    @GetMapping("/my")
    public Response<?> myPage(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        String userName = authentication.getName();
        Page postDetailResponse = postService.myPages(userName, pageable);
        return Response.success(postDetailResponse);
    }


}
