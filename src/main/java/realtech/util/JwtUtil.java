package realtech.util;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
    public static final String SECRET_KEY = "hbVyncKhHfzB9Tjt7ujG2OK8Lq49NUF69Oz44tbduw";

    public static Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public static String generatePostToken(String boardType, int postId) {
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject("Post Access") // 토큰의 주제
                .claim("boardType", boardType) // boardType 추가
                .claim("postId", postId)       // postId 추가
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(3600))) // 1시간 후 만료
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
}
