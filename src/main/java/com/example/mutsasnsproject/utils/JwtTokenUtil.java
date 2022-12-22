package com.example.mutsasnsproject.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {

    public static String getUserName(String token,String secretKey){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("userName",String.class);
    }
    public static boolean isExpired(String token,String secretKey){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration().before(new Date());
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
