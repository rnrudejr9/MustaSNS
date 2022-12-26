package com.example.mutsasnsproject.domain.dto.comment;

import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class CommentListResponse {
    private List<CommentResponse> content;
    private Pageable pageable;
}
