package com.example.mutsasnsproject.domain.entity;

import java.time.LocalDateTime;

public class Post {
    private Long id;
    private String body;
    private LocalDateTime deletedAt;
    private LocalDateTime registeredAt;
    private String title;
    private LocalDateTime updatedAt;
}
