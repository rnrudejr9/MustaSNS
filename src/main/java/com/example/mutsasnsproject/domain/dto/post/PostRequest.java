package com.example.mutsasnsproject.domain.dto.post;

import com.example.mutsasnsproject.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PostRequest {
    private String body;
    private String title;

    public Post toEntity() {
        return Post.builder()
                .body(this.body)
                .title(this.title)
                .build();
    }
}
