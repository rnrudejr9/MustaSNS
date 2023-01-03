package com.example.mutsasnsproject.domain.dto.post;

import com.example.mutsasnsproject.domain.entity.Post;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class PostResponse {
    private String message;
    private Long postId;

}
