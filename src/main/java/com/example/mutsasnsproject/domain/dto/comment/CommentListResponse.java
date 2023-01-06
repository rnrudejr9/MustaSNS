package com.example.mutsasnsproject.domain.dto.comment;

import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.entity.Comment;
import com.example.mutsasnsproject.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentListResponse {
    private Long id;
    private String userName;
    private Long postId;
    private LocalDateTime createdAt;

    public static Page<CommentListResponse> toDtoList(Page<Comment> comments){
        Page<CommentListResponse> page = comments.map(m -> CommentListResponse.builder()
                .id(m.getId())
                .userName(m.getUser().getUserName())
                .createdAt(m.getCreatedAt())
                .postId(m.getPost().getId())
                .build());
        return page;
    }
}
