package com.example.mutsasnsproject.restcontroller;

import com.example.mutsasnsproject.domain.dto.comment.CommentListResponse;
import com.example.mutsasnsproject.domain.dto.comment.CommentRequest;
import com.example.mutsasnsproject.domain.dto.comment.CommentResponse;
import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.domain.dto.post.PostResponse;
import com.example.mutsasnsproject.domain.dto.user.*;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.fixture.UserEntity;
import com.example.mutsasnsproject.infra.NotificationInterceptor;
import com.example.mutsasnsproject.service.PostService;
import com.example.mutsasnsproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserRestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;
    //자바 오브젝트를 json으로 바꿔줌

    @MockBean
    NotificationInterceptor notificationInterceptor;

    static String userName = UserEntity.get().getUserName();
    static String password = UserEntity.get().getPassword();

    //    테스트 코드
//    ----------------------------------------------------------------------------
//    회원가입 성공
//    회원가입 실패 - userName중복인 경우
//    로그인 성공
//    로그인 실패 - userName없음
//    로그인 실패 - password틀림
//    권한 변경 성공
//    권한 변경 실패 - admin 아닐 경우
//    ----------------------------------------------------------------------------

    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    void join() throws Exception {
        when(userService.join(any()))
                .thenReturn(new UserJoinResponse(1L,userName));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        //스프링시큐리티 사용할떄 꼭 넣어줘야됨
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName,password))))
                .andDo(print())
                .andExpect(jsonPath("$.result.userName").value(userName))
                .andExpect(status().isOk());
    }

    @DisplayName("회원가입 실패")
    @Test
    @WithMockUser
    void join_fail() throws Exception {

        when(userService.join(any()))
                .thenThrow(new AppException(ErrorCode.USERNAME_DUPLICATED,""));
        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName,password))))
                .andDo(print())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(status().isConflict());
    }


    @Test
    @DisplayName("로그인 성공")
    @WithMockUser
    public void login() throws Exception {
        when(userService.login(any())).thenReturn(new UserLoginResponse("jwt"));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(jsonPath("$.result.jwt").value("jwt"))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("로그인 실패 : 아이디없음")
    @WithMockUser
    //시큐리티떄문에 안넘어가는 현상 방지
    public void login2() throws Exception {
        when(userService.login(any()))
                .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND,""));
        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("로그인 실패 : 패스워드 틀림")
    @WithMockUser
    public void login3() throws Exception {
        when(userService.login(any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PASSWORD,""));
        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(status().isUnauthorized());
    }




//    권한 변경하기 ---------------------------------------

    @DisplayName("권한 변경 성공")
    @Test
    @WithMockUser
    void changeRole() throws Exception {
        when(userService.userRoleChange(any(),any(),any())).thenReturn("message");
        mockMvc.perform(post("/api/v1/users/1/role/change")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserRoleRequest("role"))))
                .andDo(print())
                .andExpect(jsonPath("$.result").value("message"))
                .andExpect(status().isOk());

    }
    @DisplayName("권한 변경 실패 : admin이 아님")
    @Test
    @WithMockUser
    void changeRole_fail() throws Exception {
        when(userService.userRoleChange(any(),any(),any())).thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));
        mockMvc.perform(post("/api/v1/users/1/role/change")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserRoleRequest("role"))))
                .andDo(print())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(status().isUnauthorized());

    }



}
