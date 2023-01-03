package com.example.mutsasnsproject.domain.dto.user;

import com.example.mutsasnsproject.domain.entity.User;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class UserJoinRequest {
    private String userName;
    private String password;

}
