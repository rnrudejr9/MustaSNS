package com.example.mutsasnsproject.controller;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.user.UserJoinRequest;
import com.example.mutsasnsproject.domain.dto.user.UserJoinResponse;
import com.example.mutsasnsproject.domain.dto.user.UserLoginRequest;
import com.example.mutsasnsproject.domain.dto.user.UserLoginResponse;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityListeners;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;
    //자바 오브젝트를 json으로 바꿔줌

    @Test
    @DisplayName("로그인 성공")
    @WithMockUser
    public void login() throws Exception {
        String userName = "hello";
        String password = "1234";
        when(userService.login(any(),any())).thenReturn(new UserLoginResponse("jwt"));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("로그인 실패 : 아이디없음")
    @WithMockUser
    //시큐리티떄문에 안넘어가는 현상 방지
    public void login2() throws Exception {
        String userName = "he";
        String password = "1234";
        when(userService.login(any(),any()))
                .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND,""));
        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("로그인 실패 : 패스워드 틀림")
    @WithMockUser
    public void login3() throws Exception {
        String userName = "hello";
        String password = "5555";
        when(userService.login(any(),any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PASSWORD,""));
        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    void join() throws Exception {
        String userName = "hello";
        String password = "1234";
        when(userService.join(any(),any()))
                .thenReturn(new UserJoinResponse(1L,"userName"));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                //스프링시큐리티 사용할떄 꼭 넣어줘야됨
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("회원가입 실패")
    @Test
    @WithMockUser
    void join_fail() throws Exception {

        String userName = "hello";
        String password = "1234";

        when(userService.join(any(),any()))
                .thenThrow(new RuntimeException("해당유저는 중복됨"));
        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isConflict());
    }

}