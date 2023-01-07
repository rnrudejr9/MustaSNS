package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.configuration.utils.JwtTokenUtils;
import com.example.mutsasnsproject.customutils.InValidChecker;
import com.example.mutsasnsproject.domain.dto.user.UserJoinRequest;
import com.example.mutsasnsproject.domain.dto.user.UserJoinResponse;
import com.example.mutsasnsproject.domain.dto.user.UserLoginRequest;
import com.example.mutsasnsproject.domain.dto.user.UserLoginResponse;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.fixture.TestInfoFixture;
import com.example.mutsasnsproject.fixture.UserEntityFixture;
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

    private static final String MOCK_TOKEN = "mockJwtToken";
    UserService userService;

    UserRepository userRepository = mock(UserRepository.class);
    AlarmRepository alarmRepository = mock(AlarmRepository.class);

    BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    InValidChecker inValidChecker = mock(InValidChecker.class);
    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository,encoder,alarmRepository,inValidChecker);
    }

//    @Test
//    @DisplayName("로그인) 성공")
//    void login(){
//        TestInfoFixture.TestInfo fixture =TestInfoFixture.get();
//        User user = mock(User.class);
//
//        when(JwtTokenUtils.generateAccessToken(any(),any(),any())).thenReturn(MOCK_TOKEN);
//        when(userRepository.findByUserName(any())).thenReturn(Optional.of(user));
//        when(encoder.matches(any(), any())).thenReturn(true);
//
//        Assertions.assertDoesNotThrow(()->userService.login(fixture.getUserName(), fixture.getPassword()));
//    }

    @Test
    @DisplayName("로그인) 실패")
    @WithAnonymousUser
    void login2(){
        TestInfoFixture.TestInfo fixture =TestInfoFixture.get();
        User user = mock(User.class);

        when(userRepository.findByUserName(any())).thenReturn(Optional.of(user));
        when(encoder.matches(any(),any())).thenReturn(true);
        when(JwtTokenUtils.generateAccessToken(any(),"secketKey",any())).thenReturn("String");

        Assertions.assertDoesNotThrow(()->userService.login(new UserLoginRequest()));
    }

    @Test
    @DisplayName("회원가입 성공")
    void join(){
        TestInfoFixture.TestInfo fixture =TestInfoFixture.get();
        User user = mock(User.class);

        Assertions.assertDoesNotThrow(()->userService.join(new UserJoinRequest()));
    }


    @Test
    @DisplayName("회원가입 실패 : 유저 중복")
    void join2(){
        TestInfoFixture.TestInfo fixture =TestInfoFixture.get();
        User user = mock(User.class);
        when(userRepository.findByUserName(any())).thenReturn(Optional.of(User.builder().build()));
        AppException exception = Assertions.assertThrows(AppException.class, () -> userService.join(new UserJoinRequest()));
        assertEquals(ErrorCode.USERNAME_DUPLICATED, exception.getErrorCode());
    }

    @Test
    @DisplayName("유저 권한 변경")
    void userRoleChange(){
        TestInfoFixture.TestInfo fixture =TestInfoFixture.get();
        User user = mock(User.class);
//        #1 통과
        when(userRepository.findByUserName(any())).thenReturn(Optional.of(user));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        Assertions.assertDoesNotThrow(()->userService.userRoleChange(any(), fixture.getUserId(), "USER"));
    }
}