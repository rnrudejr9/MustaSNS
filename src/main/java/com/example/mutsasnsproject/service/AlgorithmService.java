package com.example.mutsasnsproject.service;

import org.springframework.stereotype.Service;

@Service
public class AlgorithmService {
    public Integer sumOfDigit(int num){
        int res = 0;
        while(num / 10 != 0){
            res += num % 10;
            num = num/10;
        }
        res += num % 10;
        return res;
    }
}
