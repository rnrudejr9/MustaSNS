package com.example.mutsasnsproject.domain.dto.user;

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
}
