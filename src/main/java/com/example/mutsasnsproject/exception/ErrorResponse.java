package com.example.mutsasnsproject.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ErrorResponse {
    private ErrorCode errorCode;
    private String message;
}
