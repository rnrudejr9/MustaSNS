package com.example.mutsasnsproject.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect
public class UserRoleRequest {
    private String role;
}
