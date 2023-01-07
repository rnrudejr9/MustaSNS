package com.example.mutsasnsproject.domain.dto.comment;

import com.example.mutsasnsproject.domain.entity.Comment;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Setter
public class CommentRequest {
    private String comment;
    public Comment toEntity() {
        return Comment.builder()
                .comment(this.comment)
                .build();
    }
}
