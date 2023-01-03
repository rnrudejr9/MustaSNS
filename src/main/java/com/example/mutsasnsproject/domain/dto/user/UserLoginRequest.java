package com.example.mutsasnsproject.domain.dto.user;

import com.sun.istack.NotNull;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class UserLoginRequest {
    @NotNull
    private String userName;
    @NotNull
    private String password;
}
