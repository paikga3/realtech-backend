package realtech;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import realtech.util.JwtUtil;

public class UnitTest {
    
    @Test
    public void test() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQb3N0IEFjY2VzcyIsImJvYXJkVHlwZSI6InJlc2VydmF0aW9uX2lucXVpcnkiLCJwb3N0SWQiOjI4LCJpYXQiOjE3MzI1MzMzNjAsImV4cCI6MTczMjUzNjk2MH0.r4uUHlDcERbqQIxXYszQGcrzGVOf_iNDzwLFSA0qVIo";
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(JwtUtil.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        // boardType 및 postId 검증
        String tokenBoardType = claims.get("boardType", String.class);
        Integer tokenPostId = claims.get("postId", Integer.class);
        
        System.out.println(tokenBoardType);
    }

    
}
