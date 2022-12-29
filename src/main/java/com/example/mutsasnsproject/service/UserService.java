package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.domain.dto.user.UserJoinRequest;
import com.example.mutsasnsproject.domain.dto.user.UserJoinResponse;
import com.example.mutsasnsproject.domain.dto.user.UserLoginResponse;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.domain.role.UserRole;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.repository.UserRepository;
import com.example.mutsasnsproject.configuration.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    //join 결과에 대한 메세지를 리턴
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.secretKey}")
    private String key;

    // User 회원가입 / 로그인 기능 -------------------------------------

    public UserJoinResponse join(String userName, String password){
        // #1 userName 중복체크
        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED,userName + " 이 중복됩니다.");
                });
        // #2 Admin User일 경우
        User user;
        if(userName.equals("admin") && password.equals("admin")){
            user = User.builder()
                    .userName(userName)
                    .password(encoder.encode(password))
                    .role(UserRole.ADMIN)
                    .build();
        }else {
            user = User.builder()
                    .userName(userName)
                    .password(encoder.encode(password))
                    .role(UserRole.USER)
                    .build();
        }
        // 저장할때 저장한 데이터타입을 확인하고 Admin 이면 등급을 바꾸는 static메소드를 만들기 : 리펙토링

        userRepository.save(user);
        // 저장 후 반환할 DTO로 변환 해주는 작업 --> 엔티티를 매개변수로 받는 DTO의 메소드를 구현해보기 : 리펙토링
        UserJoinResponse userJoinResponse = UserJoinResponse
                .builder()
                .userName(user.getUserName())
                .id(user.getId())
                .build();
        return userJoinResponse;
    }

    public UserLoginResponse login(String userName,String password) {
        // #1 userName 존재하지 않을 경우
        User loginUser = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName + " 이 존재하지않습니다."));

        // #2 입력한 password 가 틀렸을 경우
        if(!encoder.matches(password,loginUser.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD,"패스워드가 일치하지 않습니다.");
        }
        long expireTimeMs = 1000 * 60 * 60L;

        String token = JwtTokenUtils.generateAccessToken(loginUser.getUserName(),key,expireTimeMs);

        // 저장 후 반환할 DTO로 변환 해주는 작업 --> 엔티티를 매개변수로 받는 DTO의 메소드를 구현해보기 : 리펙토링
        UserLoginResponse userLoginResponse = UserLoginResponse
                .builder()
                .jwt(token)
                .build();
        return userLoginResponse;
    }

    // user권한 찾기 -------------------------------------

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username).orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,"유저가 없습니다."));
    }

    // User 권한 설정 ------------------------------------

    @Transactional
    public String userRoleChange(String userName,Long userId,String userRole){
        // #1 username 없음
        User loginUser = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName+" 가 존재하지않습니다!"));

        // #2 로그인한 유저가 admin이 아닐 경우
        if(loginUser.getRole().equals(UserRole.USER)){
            System.out.println("이 값은 유저입니다");
            throw new AppException(ErrorCode.INVALID_PERMISSION,"USER 권한으로 접근하지 못합니다");
        }
        User changeableUser = userRepository.findById(userId).orElseThrow(()->new AppException(ErrorCode.DATABASE_ERROR,"해당 USER 가 존재하지않습니다."));

        // #3 body 값 오류
        if(userRole.equals("admin")) {
            changeableUser.setRole(UserRole.ADMIN);
        }else if(userRole.equals("user")){
            changeableUser.setRole(UserRole.USER);
        }else{
            throw new AppException(ErrorCode.INVALID_PERMISSION,"올바르지않은 권한 값을 입력하셨습니다.");
        }

        return changeableUser.getUserName() + " 의 USER를 " + changeableUser.getRole().name() + " 으로 등급을 조정했습니다.";
    }
}
