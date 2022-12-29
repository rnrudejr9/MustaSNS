package com.example.mutsasnsproject.controller;

import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.service.AlgorithmService;
import com.example.mutsasnsproject.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//컨트롤러 로직만 따로 검증하고 실제 호출했을때 어떤 리턴을 하는지 체크하는 테스트
//배포를 하지않고도 배포된 모습을 테스트로 확인 할 수 있어야한다.

@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    AlgorithmService algorithmService;

    @Test
    @WithMockUser
    @DisplayName("이름이 잘 돌아가는지 테스트하기")
    void hello() throws Exception {
        mockMvc.perform(get("/api/v1/hello")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("구경덕"));
    }

    @Test
    @WithMockUser
    @DisplayName("계산하는 테스트")
    void calc() throws Exception {
        when(algorithmService.sumOfDigit(687)) // 값을 지정할 수 있으나 의미가 없다.
                .thenReturn(21);
        //값을 지정할 수 있으나 별 의미가 없음
        mockMvc.perform(get("/api/v1/hello/1234").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("10"));

    }

}