package com.example.mutsasnsproject.fixture;

import com.example.mutsasnsproject.domain.entity.BaseEntity;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;

public class PostEntity extends BaseEntity {
    public static Post get(){
        return Post.builder()
                .id(1L)
                .user(UserEntity.get())
                .title("title")
                .body("body")
                .build();
    }
}
