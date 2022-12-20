package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    //join 결과에 대한 메세지를 리턴
    public String join(String userName,String password){
        Optional<User> optionalUser = userRepository.findByUserName(userName);

        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new RuntimeException(userName + "는 존재합니다");
                });
        //userName 중복체크

        User user = User.builder()
                .userName(userName)
                .password(password)
                .registeredAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        //저장
        return "회원가입에 성공했습니다.";
    }
}
