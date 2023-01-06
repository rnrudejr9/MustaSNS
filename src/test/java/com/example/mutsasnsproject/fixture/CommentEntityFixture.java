package com.example.mutsasnsproject.fixture;

import com.example.mutsasnsproject.domain.entity.BaseEntity;
import com.example.mutsasnsproject.domain.entity.Comment;
import com.example.mutsasnsproject.domain.entity.Post;


public class CommentEntityFixture extends BaseEntity {
    public static Comment get(String comment) {

        return Comment.builder()
                .comment(comment)
                .user(UserEntityFixture.get(TestInfoFixture.get().getUserName(), TestInfoFixture.get().getPassword()))
                .post(PostEntityFixture.get(TestInfoFixture.get().getUserName(), TestInfoFixture.get().getPassword()))
                .build();
    }
}