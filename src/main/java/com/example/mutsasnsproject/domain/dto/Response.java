package com.example.mutsasnsproject.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Response<T>{
    private String resultCode;
    private T result;
}
