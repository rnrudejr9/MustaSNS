package com.example.mutsasnsproject.domain.dto.post;

import com.example.mutsasnsproject.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Builder
@Getter
public class PostDetailResponse {
    private Long id;
    private String title;
    private String body;
    private String userName;
    private String createdAt;
    private String lastModifiedAt;
    public static Page<PostDetailResponse> toDtoList(Page<Post> postEntities){
        Page<PostDetailResponse> postDetailResponses = postEntities.map(m -> PostDetailResponse.builder()
                .id(m.getId())
                .title(m.getTitle())
                .body(m.getBody())
                .userName(m.getUser().getUserName())
                .createdAt(m.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")))
                .build());
        return postDetailResponses;
    }

}
