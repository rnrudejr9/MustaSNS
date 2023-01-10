package com.example.mutsasnsproject.fixture;

import com.example.mutsasnsproject.domain.entity.BaseEntity;
import com.example.mutsasnsproject.domain.entity.Comment;

public class CommentEntity extends BaseEntity {
    public static Comment get(){
        return Comment.builder()
                .comment("comment")
                .user(UserEntity.get())
                .post(PostEntity.get())
                .build();
    }
}
