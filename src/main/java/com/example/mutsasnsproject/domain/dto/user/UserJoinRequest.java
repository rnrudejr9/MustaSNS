package com.example.mutsasnsproject.domain.dto.user;

import com.example.mutsasnsproject.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserJoinRequest {
    private String userName;
    private String password;

}
