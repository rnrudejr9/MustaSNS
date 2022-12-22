package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.post.PostResponse;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    public Response<?> createPost(String body, String title){
        Post savedPost = Post.builder()
                .body(body)
                .registeredAt(LocalDateTime.now())
                .title(title)
                .build();
        postRepository.save(savedPost);
        PostResponse postResponse = PostResponse.builder()
                .id(savedPost.getId())
                .message("포스트 작성완료")
                .build();
        return Response.success(postResponse);
    }
}
