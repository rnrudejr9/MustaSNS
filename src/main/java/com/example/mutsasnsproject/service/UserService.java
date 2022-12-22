package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.user.UserJoinResponse;
import com.example.mutsasnsproject.domain.dto.user.UserLoginResponse;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.domain.role.UserRole;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.repository.UserRepository;
import com.example.mutsasnsproject.configuration.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    //join 결과에 대한 메세지를 리턴
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.secretKey}")
    private String key;
    public Response<?> join(String userName, String password){
        Optional<User> optionalUser = userRepository.findByUserName(userName);

        //userName 중복체크
        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED,userName+"이름이 중복됩니다.");
                });

        //저장
        User user = User.builder()
                .userName(userName)
                .password(encoder.encode(password))
                .registeredAt(LocalDateTime.now())
                .role(UserRole.USER)
                .build();
        userRepository.save(user);
        UserJoinResponse userJoinResponse = UserJoinResponse
                .builder()
                .userName(user.getUserName())
                .id(user.getId())
                .build();
        return new Response<>("UserJoinSuccess",userJoinResponse);
    }

    public Response<?> login(String userName,String password) {
        //username 없음
        User loginUser = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName+"없습니다.!"));

        //password 틀림
        if(!encoder.matches(password,loginUser.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD,"패스워드 오류");
        }
        long expireTimeMs = 1000 * 60 * 60L;
        String token = JwtTokenUtil.createToken(loginUser.getUserName(),key,expireTimeMs);
        UserLoginResponse userLoginResponse = UserLoginResponse
                .builder()
                .jwt(token)
                .build();
        //앞에서 예외처리 안되었으면 토큰 발행
        return new Response<>("UserLoginSuccess",userLoginResponse);
    }



}
