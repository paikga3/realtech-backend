package realtech.api.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import realtech.api.common.model.ErrorResponse;
import realtech.api.common.model.PostPasswordRequest;
import realtech.api.common.model.PostPasswordResponse;
import realtech.api.common.model.ValidateTokenRequest;
import realtech.api.common.service.AuthTokenService;
import realtech.util.AesEncryptionUtil;
import realtech.util.JwtUtil;
import realtech.util.JwtValidator;

@RestController
public class AuthTokenController {
    
    @Autowired
    private AuthTokenService authTokenService;
    
    @GetMapping("/api/{boardType}/{postId}/validate-password")
    public ResponseEntity<?> validatePassword(
            @PathVariable String boardType,
            @PathVariable int postId,
            PostPasswordRequest request) throws Exception {
        boolean isValid = authTokenService.validatePassword(boardType, postId, request.getPassword());
        
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("비밀번호가 틀립니다."));
        }
        
        String token = AesEncryptionUtil.encode(JwtUtil.generateToken(boardType, postId));
        return ResponseEntity.ok(new PostPasswordResponse(token));
    }
    
    @GetMapping("/api/validate-token")
    public ResponseEntity<Boolean> validateToken(ValidateTokenRequest request) {
        try {
            String decodeToken = AesEncryptionUtil.decode(request.getToken());
            // JwtValidator.validateToken 호출
            JwtValidator.validateToken(decodeToken, request.getBoardType(), request.getPostId());
            // 예외가 발생하지 않으면 true 반환
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            // 예외 발생 시 false 반환
            return ResponseEntity.ok(false);
        }
    }
}
