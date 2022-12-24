package com.example.mutsasnsproject.configuration;

import com.example.mutsasnsproject.configuration.utils.JwtTokenUtil;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final UserService userService;

    private final String secretKey;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("a "+ authorization);
        final String token;
        //인증이 없으면 바로 리턴
        try {
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                log.error("비엇거나 유효하지않은 암호");
                filterChain.doFilter(request, response);
                return;
                //여기서 리턴하면 어디로가 ??..?
            } else {
                token = authorization.split(" ")[1].trim();
                System.out.println("token : " + token);
            }
            String userName = JwtTokenUtil.getUserName(token, secretKey);
            User userDetails = userService.loadUserByUsername(userName);

            //Token expire 체크
            if (!JwtTokenUtil.validate(token, userDetails.getUsername(), secretKey)) {
                log.error("만료되었거나 이상한 암호");
                filterChain.doFilter(request, response);
                return;
            }


            //userName 토큰에서 꺼내기

            //권한 부여
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            //Detail
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        }catch (Exception e){
            filterChain.doFilter(request,response);
        }

    }
}