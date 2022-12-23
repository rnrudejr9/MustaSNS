package com.example.mutsasnsproject.configuration;

import com.example.mutsasnsproject.domain.role.UserRole;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig {
    private final UserService userService;
    private final String[] SWAGGER = {
            "/v3/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger/**"
    };

    public static final String[] GET_BASIC = {
            "/api/v1/posts/**"
    };

    public static final String[] BASIC = {
            "/api/v1/hello",
            "/api/v1/users/login",
            "/api/v1/users/join"
    };
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

        try {
            return httpSecurity
                    .httpBasic().disable()
                    //ui 쪽 들어오는 disable
                    .csrf().disable()
                    //크로스사이트 disable
                    .cors().and()
                    //도메인달라도 허용해준다
                    .authorizeRequests()
                    .antMatchers(SWAGGER).permitAll()
                    .antMatchers(BASIC).permitAll()
                    .antMatchers(HttpMethod.GET, GET_BASIC).permitAll()
                    //로그인, 회원가입은 허용
                    .antMatchers(HttpMethod.POST, "/api/v1/posts").authenticated()
                    .anyRequest().authenticated()
                    //게시글 작성은 로그인 필요!
                    //허용해주는 단계
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .addFilterBefore(new JwtFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)
                    .build();
        }catch (Exception e){
            throw new AppException(ErrorCode.INVALID_PERMISSION,"접근불가띠");
        }
    }
}

