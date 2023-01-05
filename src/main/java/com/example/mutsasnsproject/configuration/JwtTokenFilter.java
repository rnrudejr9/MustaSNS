package com.example.mutsasnsproject.configuration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.mutsasnsproject.configuration.utils.JwtTokenUtils;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
//    private UserDetailsService userDetailsService;

    private final UserService userService;

    private final String secretKey;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;
        HttpSession session = null;
        // authorizationHeader에 "Bearer + JwtToken"이 제대로 들어왔는지 체크
        if(header == null) {
            // 화면 로그인을 위해 Session에서 Token을 꺼내보는 작업 => 여기에도 없으면 인증 실패
            // 여기에 있으면 이 Token으로 인증 진행
            session = request.getSession(false);
            if(session == null || session.getAttribute("jwt") == null) {
                chain.doFilter(request, response);
                return;
            } else {
                header = request.getSession().getAttribute("jwt").toString();
            }
        }

        if (!header.startsWith("Bearer ")) {
            log.error("Authorization Header does not start with Bearer {}", request.getRequestURI());
            chain.doFilter(request, response);
            return;
        } else {
            token = header.split(" ")[1].trim();
            System.out.println(token);
        }

        try {
            String userName = JwtTokenUtils.getUsername(token, secretKey);
            User userDetails = userService.loadUserByUsername(userName);
            if (!JwtTokenUtils.validate(token, userDetails.getUserName(), secretKey)) {
                chain.doFilter(request, response);
                return;
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails.getUserName(), null,
                    userDetails.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }catch (ExpiredJwtException e){
            session.removeAttribute("jwt");
            session.invalidate();
            chain.doFilter(request,response);
        }finally {
            return;
        }
    }

}


