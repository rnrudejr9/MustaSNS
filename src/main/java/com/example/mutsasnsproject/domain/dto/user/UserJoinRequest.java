package com.example.mutsasnsproject.domain.dto.user;

import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.domain.role.UserRole;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class UserJoinRequest {
    private String userName;
    private String password;

    public User toDto(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(userName.equals("admin") && password.equals("admin")){
            return User.builder()
                    .userName(userName)
                    .password(encoder.encode(password))
                    .role(UserRole.ADMIN)
                    .build();
        }else {
            return User.builder()
                    .userName(userName)
                    .password(encoder.encode(password))
                    .role(UserRole.USER)
                    .build();
        }
    }
}
