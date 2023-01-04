package com.example.mutsasnsproject.configuration;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.role.UserRole;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.beans.Encoder;
import java.io.IOException;

//import com.example.demo.configuration.filter.JwtTokenfilter;
//import com.example.demo.utils.JwtTokenUtil;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    @Value("${jwt.secretKey}")
    private String secretKey;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/api/v1/users/join", "/api/v1/users/login").permitAll()
//                .antMatchers("/api/*/users/alarm/subscribe/*").permitAll()
                .antMatchers(HttpMethod.POST,"/api/v1/posts/**", "/api/v1/posts").authenticated()
                .antMatchers(HttpMethod.DELETE,"/api/v1/posts/**").authenticated()
                .antMatchers(HttpMethod.PUT,"/api/v1/posts/**").authenticated()
                .antMatchers(HttpMethod.POST, "/api/v1/users/**/role/change").hasAuthority(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.POST, "/api/v1/posts/**/comment").authenticated()

                //UI
                .antMatchers("/view/v1/users/login").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/view/v1/posts/form").authenticated()
                .anyRequest().permitAll()
                .and()

                .formLogin()
                .loginPage("/view/v1/users/login")    //로그인 페이지
                .defaultSuccessUrl("/view/v1/posts/list") //로그인 성공 후
                .and()
                .logout()
                .logoutSuccessUrl("/view/v1/posts/login")//로그아웃 성공
                .and()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
                        makeErrorResponse(response, ErrorCode.INVALID_PERMISSION);
                    }
                })
                // 인가 실패 시 INVALID_PERMISSION 에러 발생
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
                        makeErrorResponse(response, ErrorCode.INVALID_PERMISSION);
                    }
                })
                .and()
                .build();
    }

    //UI를 위한 jdbc 로그인 인증 절차



    public void makeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), Response.error("ERROR",errorCode));
    }

}


