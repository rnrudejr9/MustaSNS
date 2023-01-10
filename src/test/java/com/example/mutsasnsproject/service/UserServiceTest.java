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
import com.example.mutsasnsproject.fixture.UserEntity;
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
import static org.mockito.Mockito.*;

class UserServiceTest {

    private static final String MOCK_TOKEN = "mockJwtToken";
    UserService userService;

    UserRepository userRepository = mock(UserRepository.class);
    AlarmRepository alarmRepository = mock(AlarmRepository.class);

    BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    InValidChecker inValidChecker = mock(InValidChecker.class);

    User user;
    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository,encoder,alarmRepository,inValidChecker);
        user = UserEntity.get();
    }

    //    테스트 코드
//    ----------------------------------------------------------------------------
//    로그인 실패 유저 없을 경우
//    로그인 실패 비밀번호 다를 경우
//    회원가입 성공
//    회원가입 실패 유저 중복
//    ----------------------------------------------------------------------------
    @Test
    @DisplayName("로그인 실패 : 유저 없는 경우")
    @WithAnonymousUser
    void login_fail1(){
        when(inValidChecker.userCheck(any())).thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND,""));
        AppException exception = Assertions.assertThrows(AppException.class, () -> userService.login(new UserLoginRequest()));
        assertEquals(ErrorCode.USERNAME_NOT_FOUND,exception.getErrorCode());
    }

    @Test
    @DisplayName("로그인 실패 : 비밀번호 다를 경우")
    @WithAnonymousUser
    void login_fail2(){
        when(inValidChecker.userCheck(any())).thenReturn(user);
        doThrow(new AppException(ErrorCode.INVALID_PASSWORD,"")).when(inValidChecker).passwordCheck(any(),any());
        AppException exception = Assertions.assertThrows(AppException.class, () -> userService.login(new UserLoginRequest()));
        assertEquals(ErrorCode.INVALID_PASSWORD,exception.getErrorCode());
    }

    @Test
    @DisplayName("회원가입 성공")
    void join(){
        Assertions.assertDoesNotThrow(()->userService.join(new UserJoinRequest("userName","password")));
    }


    @Test
    @DisplayName("회원가입 실패 : 유저 중복")
    void join_fail(){;
        doThrow(new AppException(ErrorCode.USERNAME_DUPLICATED,"")).when(inValidChecker).isDuplicatedUserName(any());
        AppException exception = Assertions.assertThrows(AppException.class, () -> userService.join(new UserJoinRequest()));
        assertEquals(ErrorCode.USERNAME_DUPLICATED, exception.getErrorCode());
    }
}