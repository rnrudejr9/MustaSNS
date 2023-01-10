package com.example.mutsasnsproject.fixture;

import com.example.mutsasnsproject.domain.entity.BaseEntity;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.domain.role.UserRole;

public class UserEntity extends BaseEntity {
    public static User get(){
        return User.builder()
                .id(1L)
                .password("password")
                .userName("userName")
                .role(UserRole.USER)
                .build();
    }
}
