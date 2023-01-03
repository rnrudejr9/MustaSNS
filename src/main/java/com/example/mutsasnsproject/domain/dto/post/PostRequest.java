package com.example.mutsasnsproject.domain.dto.post;

import com.example.mutsasnsproject.domain.entity.Post;
import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.Size;

@Builder
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class PostRequest {


    @NotNull
    @Size(min=1,max=300)
    private String body;
    @NotNull
    @Size(min=1,max=30)
    private String title;

    public Post toEntity() {
        return Post.builder()
                .body(this.body)
                .title(this.title)
                .build();
    }
}
