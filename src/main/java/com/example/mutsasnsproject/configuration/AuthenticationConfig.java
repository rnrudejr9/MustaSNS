package com.example.mutsasnsproject.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AuthenticationConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .httpBasic().disable()
                //ui 쪽 들어오는 disable
                .csrf().disable()
                //크로스사이트 disable
                .cors().and()
                //도메인달라도 허용해준다
                .authorizeRequests()
                .antMatchers("/api/v1/users/join","/api/v1/users/login").permitAll()
                //로그인, 회원가입은 허용
                .antMatchers(HttpMethod.POST,"/api/v1/posts").authenticated()
                //게시글 작성은 로그인 필요!
                //허용해주는 단계
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                .addFilterBefore(new JwtTokenFilter(userService,secretKey), UsernamePasswordAuthenticationFilter)
                .build();
    }
}

