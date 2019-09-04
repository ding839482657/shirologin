package com.gnjf.shirologin.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    public final static String ACCOUNT = "account";
    public final static String role = "role";

    private String key = "jfjfjf";

    // 有效期 10 秒
//    private long expirationTime = 10;
    private long expirationTime = 3600;

    // 生成JWT
    public String createJWT(String id, String username,String role) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .setId(id)
//                主题
                .setSubject(username)
//                创建的时间
                .setIssuedAt(now)
                .claim(JwtUtil.role,role)

                .claim(JwtUtil.ACCOUNT, username)
//                加密方式
                .signWith(SignatureAlgorithm.HS256,key);
        builder.setExpiration(new Date(System.currentTimeMillis() + (expirationTime * 1000)));
//      令牌生成
        return builder.compact();
    }

    // 解析JWT
    public Claims parseJWT(String jwtStr) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtStr)
                .getBody();
    }
}
