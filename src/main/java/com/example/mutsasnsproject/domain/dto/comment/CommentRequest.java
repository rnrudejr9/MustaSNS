package com.example.mutsasnsproject.domain.dto.comment;

import com.example.mutsasnsproject.domain.entity.Comment;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect
@Builder
public class CommentRequest {
    private String comment;
    public Comment toEntity() {
        return Comment.builder()
                .comment(this.comment)
                .build();
    }
}
