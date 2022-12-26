package com.example.mutsasnsproject.domain.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class CommentResponse {
    private Long id;
    private String comment;
    private String userName;
    private Long postId;
    private String createdAt;
}
