package com.example.mutsasnsproject.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmServiceTest {

//    spring 안쓰고 테스트 하기 위함 new 로 할당한다
//            pojo 방식 최대한 활용
    AlgorithmService algorithmService = new AlgorithmService();

    @Test
    @DisplayName("자릿수 잘 구하는지 테스트")
    void sumOfDigit(){
        assertEquals(21, algorithmService.sumOfDigit(687));
        assertEquals(22, algorithmService.sumOfDigit(787));
        assertEquals(0, algorithmService.sumOfDigit(0));
    }
}