package com.example.mutsasnsproject.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class PostResponse {
    private Long id;
    private String message;
}
