package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.configuration.utils.JwtTokenUtils;
import com.example.mutsasnsproject.domain.dto.user.UserJoinResponse;
import com.example.mutsasnsproject.domain.dto.user.UserLoginResponse;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.fixture.TestInfoFixture;
import com.example.mutsasnsproject.repository.AlarmRepository;
import com.example.mutsasnsproject.repository.CommentRepository;
import com.example.mutsasnsproject.repository.PostRepository;
import com.example.mutsasnsproject.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    UserService userService;

    UserRepository userRepository = mock(UserRepository.class);
    AlarmRepository alarmRepository = mock(AlarmRepository.class);

    BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, encoder, alarmRepository);
    }

    @Test
    @DisplayName("로그인) 성공")
    @WithMockUser
    void login(){
        TestInfoFixture.TestInfo fixture =TestInfoFixture.get();
        User user = mock(User.class);

        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(user));
        when(encoder.matches(fixture.getUserName(),fixture.getPassword())).thenReturn(true);
//        when(JwtTokenUtils.generateAccessToken(any(),"secketKey",any())).thenReturn("String");

        Assertions.assertDoesNotThrow(()->userService.login(fixture.getUserName(), fixture.getPassword()));
    }

    @Test
    @DisplayName("로그인) 실패")
    @WithAnonymousUser
    void login2(){
        TestInfoFixture.TestInfo fixture =TestInfoFixture.get();
        User user = mock(User.class);

        when(userRepository.findByUserName(any())).thenReturn(Optional.of(user));
        when(encoder.matches(any(),any())).thenReturn(true);
        when(JwtTokenUtils.generateAccessToken(any(),"secketKey",any())).thenReturn("String");

        Assertions.assertDoesNotThrow(()->userService.login(fixture.getUserName(), fixture.getPassword()));
    }

    @Test
    @DisplayName("회원가입 성공")
    void join(){
        TestInfoFixture.TestInfo fixture =TestInfoFixture.get();
        User user = mock(User.class);

        Assertions.assertDoesNotThrow(()->userService.join(fixture.getUserName(), fixture.getPassword()));
    }


    @Test
    @DisplayName("회원가입 실패 : 유저 중복")
    void join2(){
        TestInfoFixture.TestInfo fixture =TestInfoFixture.get();
        User user = mock(User.class);
        when(userRepository.findByUserName(any())).thenReturn(Optional.of(User.builder().build()));
        AppException exception = Assertions.assertThrows(AppException.class, () -> userService.join("",""));
        assertEquals(ErrorCode.USERNAME_DUPLICATED, exception.getErrorCode());
    }
}