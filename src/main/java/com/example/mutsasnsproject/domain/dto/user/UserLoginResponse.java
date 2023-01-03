package com.example.mutsasnsproject.domain.dto.user;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class UserLoginResponse {
    private String jwt;
}
