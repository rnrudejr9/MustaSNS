package com.example.mutsasnsproject.domain.dto.post;

import com.example.mutsasnsproject.domain.entity.Post;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
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
