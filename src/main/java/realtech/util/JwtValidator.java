package realtech.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import realtech.api.common.exception.InvalidJwtException;
import realtech.api.common.exception.JwtExpiredException;

public class JwtValidator {
    /**
     * JWT 토큰 검증 메서드
     *
     * @param token   JWT 토큰
     * @param boardType 검증할 게시판 유형 (예: "reservation_inquiry")
     * @param postId    검증할 게시글 ID
     */
    public static void validatePostToken(String token, String boardType, int postId) {
        System.out.println(token);
        try {
            // 토큰 파싱 및 클레임 추출
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JwtUtil.getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // boardType 및 postId 검증
            String tokenBoardType = claims.get("boardType", String.class);
            Integer tokenPostId = claims.get("postId", Integer.class);

            if (!boardType.equals(tokenBoardType)) {
                throw new InvalidJwtException("Invalid board type in JWT token.");
            }

            if (postId != tokenPostId) {
                throw new InvalidJwtException("Invalid post ID in JWT token.");
            }

        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException("JWT token has expired.");
        } catch (Exception e) {
            throw new InvalidJwtException("Invalid JWT token: " + e.getMessage());
        }
    }
    
    
    public static Claims validateToken(String token) {
        try {
            // 토큰 파싱 및 클레임 추출
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JwtUtil.getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String role = claims.get("role", String.class);

            if (!"admin".equals(role)) {
                throw new InvalidJwtException("Forbidden: Not an admin");
            }
            
            return claims;

        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException("JWT token has expired.");
        } catch (Exception e) {
            throw new InvalidJwtException("Invalid JWT token: " + e.getMessage());
        }
    }
}
