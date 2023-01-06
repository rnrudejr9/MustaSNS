package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.customutils.InValidChecker;
import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.alarm.AlarmResponse;
import com.example.mutsasnsproject.domain.dto.user.UserJoinRequest;
import com.example.mutsasnsproject.domain.dto.user.UserJoinResponse;
import com.example.mutsasnsproject.domain.dto.user.UserLoginRequest;
import com.example.mutsasnsproject.domain.dto.user.UserLoginResponse;
import com.example.mutsasnsproject.domain.entity.Alarm;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.domain.role.UserRole;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.repository.AlarmRepository;
import com.example.mutsasnsproject.repository.UserRepository;
import com.example.mutsasnsproject.configuration.utils.JwtTokenUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    //join 결과에 대한 메세지를 리턴
    private final BCryptPasswordEncoder encoder;
    private final AlarmRepository alarmRepository;

    private final InValidChecker inValidChecker;
    @Value("${jwt.secretKey}")
    private String key;

    // User 회원가입 / 로그인 기능 -------------------------------------

    // 저장할때 저장한 데이터타입을 확인하고 Admin 이면 등급을 바꾸는 static메소드를 만들기 : 리펙토링
    // 저장 후 반환할 DTO로 변환 해주는 작업 --> 엔티티를 매개변수로 받는 DTO의 메소드를 구현해보기 : 리펙토링 완료
    public UserJoinResponse join(UserJoinRequest userJoinRequest){
        inValidChecker.isDuplicatedUserName(userJoinRequest.getUserName());
        User user = userJoinRequest.toDto();
        userRepository.save(user);
        return UserJoinResponse.of(user);
    }

    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        User loginUser = inValidChecker.userCheck(userLoginRequest.getUserName());
        inValidChecker.passwordCheck(userLoginRequest.getPassword(),loginUser.getPassword());
        long expireTimeMs = 1000 * 60 * 60L;
        String token = JwtTokenUtils.generateAccessToken(loginUser.getUserName(),key,expireTimeMs);

        UserLoginResponse userLoginResponse = UserLoginResponse.builder().jwt(token).build();
        return userLoginResponse;
    }

    // user권한 찾기 -------------------------------------

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return inValidChecker.userCheck(username);
    }

    // User 권한 설정 ------------------------------------

    @Transactional
    public String userRoleChange(String userName,Long userId,String userRole){

        User loginUser = inValidChecker.userCheck(userName);

        if(loginUser.getRole().equals(UserRole.USER)){
            System.out.println("이 값은 유저입니다");
            throw new AppException(ErrorCode.INVALID_PERMISSION,"USER 권한으로 접근하지 못합니다");
        }
        User changeableUser = inValidChecker.userCheckById(userId);

        if(userRole.equals("admin")) {
            changeableUser.setRole(UserRole.ADMIN);
        }else if(userRole.equals("user")){
            changeableUser.setRole(UserRole.USER);
        }else{
            throw new AppException(ErrorCode.INVALID_PERMISSION,"올바르지않은 권한 값을 입력하셨습니다.");
        }

        return changeableUser.getUserName() + " 의 USER를 " + changeableUser.getRole().name() + " 으로 등급을 조정했습니다.";
    }

    //    알람 조회 기능 ------------------------------------------------------

    public Page<AlarmResponse> getAlarm(String userName, Pageable pageable){
        // #1 토큰으로 로그인한 아이디가 없을 경우
        User user = inValidChecker.userCheck(userName);
        Page<Alarm> page = alarmRepository.findByUser(pageable,user);
        Page<AlarmResponse> alarmResponsePage = AlarmResponse.toDtoList(page);
        return alarmResponsePage;
    }

}
