package com.example.mutsasnsproject.domain.entity;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private String comment;
    private LocalDateTime deletedAt;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
}
