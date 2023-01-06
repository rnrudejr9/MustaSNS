package com.example.mutsasnsproject.domain.dto.user;

import com.example.mutsasnsproject.domain.entity.User;
import lombok.*;
import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class UserJoinResponse {
    private Long id;
    private String userName;

    public static UserJoinResponse of(User user){
        return UserJoinResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .build();
    }
}
