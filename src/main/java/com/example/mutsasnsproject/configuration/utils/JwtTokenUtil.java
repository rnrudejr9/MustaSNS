package com.example.mutsasnsproject.configuration.utils;

import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {

    public static String getUserName(String token,String secretKey){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("userName",String.class);
    }
    public static boolean validate(String token,String userName,String secretKey){
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.SignatureException | MalformedJwtException exception) { // 잘못된 jwt signature
        } catch (io.jsonwebtoken.ExpiredJwtException exception) { // jwt 만료
        } catch (io.jsonwebtoken.UnsupportedJwtException exception) { // 지원하지 않는 jwt
        } catch (IllegalArgumentException exception) { // 잘못된 jwt 토큰
        }

        new AppException(ErrorCode.INVALID_TOKEN,"validate");
        return false;
    }
    public static String getUsername(String token, String key) {
        return extractAllClaims(token, key).get("username", String.class);
    }
    public static Boolean isTokenExpired(String token, String key) {
        Date expiration = extractAllClaims(token, key).getExpiration();
        return expiration.before(new Date());
    }
    public static Claims extractAllClaims(String token, String key) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }



    public static String createToken(String userName,String key,long expireTimeMs){
        Claims claims = Jwts.claims();
        //일종의 map 같은 것
        claims.put("userName",userName);

        return Jwts.builder()
                .setClaims(claims)
                //클레임을 지정하고
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //만든날짜
                .setExpiration(new Date(System.currentTimeMillis()+expireTimeMs))
                //종료일자
                .signWith(SignatureAlgorithm.HS256,key)
                //암호화
                .compact()
                ;

    }
}
