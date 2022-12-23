package com.example.mutsasnsproject.domain.dto.post;

import com.example.mutsasnsproject.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class PostListResponse {
    private List<PostDetailResponse> content;
    private Pageable pageable;

}
