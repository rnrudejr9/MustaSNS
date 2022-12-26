package com.example.mutsasnsproject.fixture;

import com.example.mutsasnsproject.domain.entity.BaseEntity;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.role.UserRole;


import java.sql.Timestamp;
import java.time.Instant;

public class PostEntityFixture extends BaseEntity {
    public static Post get(String userName, String password) {
        Post postEntity = Post.builder()
                .id(1L)
                .user(UserEntityFixture.get(userName, password))
                .title("title")
                .body("body")
                .build();
        return postEntity;
    }
}

