package com.yh.blogserver.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final long accessTokenValidityMS = 1000L * 60 * 10;
    // ms * s * m * H * d
    private final long refreshTokenValidityMS = 1000L * 60 * 60 * 24 * 7;

    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        // Base64 디코딩 후 SecretKey 생성
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    // header 는 다음 값으로 자동으로 생성하며, .claim() 으로 페이로드를 추가함.
    //{
    //  "alg": "HS256",
    //  "typ": "JWT"
    //}
    public String createToken(String userId, boolean isAdmin) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValidityMS);

        return Jwts.builder()
                .header().add("typ", "JWT")
                .and()
                .claim("userId", userId)
                .claim("isAdmin", isAdmin)
                .setIssuedAt(now)
                .setExpiration(expiry)
                // signature 생성부 ->
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String userId) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenValidityMS);

        return Jwts.builder()
                .header().add("typ", "JWT")
                .and()
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
//        } catch (ExpiredJwtException e){
//            exception 처리 생각하기
        }
    }

    public String getUserIdFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", String.class);
    }

    public boolean getUserGrantFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("isAdmin", Boolean.class);
    }
}
