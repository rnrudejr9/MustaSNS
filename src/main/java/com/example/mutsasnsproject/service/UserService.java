package com.example.mutsasnsproject.service;

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
    public UserJoinResponse join(String userName, String password){
        Optional<User> optionalUser = userRepository.findByUserName(userName);

        // #1 userName 중복체크
        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED,userName+"이름이 중복됩니다.");
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
            //저장
            user = User.builder()
                    .userName(userName)
                    .password(encoder.encode(password))
                    .role(UserRole.USER)
                    .build();
        }
        userRepository.save(user);
        UserJoinResponse userJoinResponse = UserJoinResponse
                .builder()
                .userName(user.getUserName())
                .id(user.getId())
                .build();
        return userJoinResponse;
    }

    public UserLoginResponse login(String userName,String password) {
        //username 없음
        User loginUser = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName+"없습니다.!"));

        //password 틀림
        if(!encoder.matches(password,loginUser.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD,"패스워드 오류");
        }
        long expireTimeMs = 1000 * 60 * 60L;
        String token = JwtTokenUtils.generateAccessToken(loginUser.getUserName(),key,expireTimeMs);
        UserLoginResponse userLoginResponse = UserLoginResponse
                .builder()
                .jwt(token)
                .build();
        //앞에서 예외처리 안되었으면 토큰 발행
        return userLoginResponse;
    }

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username).orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,"유저가 없습니다."));
    }

    @Transactional
    public String userRoleChange(String userName,Long userId,String userRole){
        // #1 username 없음
        User loginUser = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName+"없습니다.!"));

        // #2 로그인한 유저가 admin이 아닐 경우
        if(loginUser.getRole().equals(UserRole.USER)){
            System.out.println("이 값은 유저입니다");
            throw new AppException(ErrorCode.INVALID_PERMISSION,"유저가 접근하지 못합니다");
        }

        // #3 body 값 오류
        if(!userRole.equals("admin")){
            throw new AppException(ErrorCode.INVALID_PERMISSION,"BODY값에 잘못된 등급을 입력했습니다.");
        }

        User changeableUser = userRepository.findById(userId).orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,"해당유저가 없습니다"));

        // #3 설정할 user가 이미 admin 일 경우
        if(changeableUser.getRole().equals(UserRole.ADMIN)){
            throw new AppException(ErrorCode.INVALID_PERMISSION,"이미 admin 계정으로 설정되어있습니다.");
        }
        changeableUser.setRole(UserRole.ADMIN);
        return changeableUser.getUserName() + " 의 USER를 ADMIN으로 등급을 올렸습니다.";
    }
}
