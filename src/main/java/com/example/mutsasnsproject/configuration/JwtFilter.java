package com.example.mutsasnsproject.configuration;

import com.example.mutsasnsproject.service.UserService;
import com.example.mutsasnsproject.configuration.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter{
    private final UserService userService;

    private final String secretKey;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("a "+ authorization);


        //인증이 없으면 바로 리턴
        if(authorization == null || !authorization.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        //token 꺼내기
        String token = authorization.split(" ")[1];

        if(!JwtTokenUtil.isInvalid(token,secretKey)){
            filterChain.doFilter(request,response);
            return;
        }
        //Token expire 체크
//        if(JwtTokenUtil.isExpired(token,secretKey)){
//            filterChain.doFilter(request,response);
//            return;
//        }

        //userName 토큰에서 꺼내기
        String userName = JwtTokenUtil.getUserName(token,secretKey);

        //권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userName,null, List.of(new SimpleGrantedAuthority("USER")));
        //Detail
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }
}
