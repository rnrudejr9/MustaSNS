package com.example.mutsasnsproject.fixture;

import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.domain.role.UserRole;


import java.sql.Timestamp;
import java.time.Instant;

public class UserEntityFixture {

    public static User get(String userName, String password) {
        User entity = new User();
        entity.setId(1L);
        entity.setUserName(userName);
        entity.setPassword(password);
        entity.setRole(UserRole.USER);
        return entity;
    }
}
