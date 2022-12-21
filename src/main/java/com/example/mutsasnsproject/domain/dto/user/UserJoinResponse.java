package com.example.mutsasnsproject.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class UserJoinResponse {
    private String userName;
    private LocalDateTime createdAt;
}
