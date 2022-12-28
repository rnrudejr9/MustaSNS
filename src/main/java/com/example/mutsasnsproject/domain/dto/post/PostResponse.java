package com.example.mutsasnsproject.domain.dto.post;

import com.example.mutsasnsproject.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class PostResponse {
    private String message;
    private Long postId;

}
