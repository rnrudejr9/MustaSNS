package com.example.mutsasnsproject.domain.dto.user;

import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.domain.role.UserRole;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.format.DateTimeFormatter;
@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class UserResponse {
    String userName;
    Long id;
    String password;
    String createdAt;
    UserRole userRole;
    int postCount;
    int commentCount;
    int goodCount;

    public static Page<UserResponse> toDtoList(Page<User> userEntity) {
        Page<UserResponse> userResponses = userEntity.map(m -> UserResponse.builder()
                .id(m.getId())
                .userName(m.getUserName())
                .createdAt(m.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .userRole(m.getRole())
                .postCount(m.getPost().size())
                .commentCount(m.getComments().size())
                .goodCount(m.getGoods().size())
                .build());
        return userResponses;
    }
}
