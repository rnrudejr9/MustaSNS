package com.example.mutsasnsproject.fixture;

import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.role.UserRole;

public class PostEntityFixture {
    public static Post get(String userName, String password) {
        Post entity = new Post();
        entity.setId(1L);
        entity.setBody("body");
        entity.setTitle("title");
        entity.setUser(UserEntityFixture.get(userName,password));
        entity.setCreatedAt(UserEntityFixture.get(userName,password).getCreatedAt());
        entity.setCreatedAt(UserEntityFixture.get(userName,password).getLastModifiedAt());
        return entity;
    }
}

