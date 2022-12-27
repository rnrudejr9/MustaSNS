package com.example.mutsasnsproject.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@JsonAutoDetect
@NoArgsConstructor
public class UserRoleRequest {
    private String role;
}
