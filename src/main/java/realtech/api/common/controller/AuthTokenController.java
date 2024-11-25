package realtech.api.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import realtech.api.common.model.ErrorResponse;
import realtech.api.common.model.PostPasswordRequest;
import realtech.api.common.model.PostPasswordResponse;
import realtech.api.common.service.AuthTokenService;
import realtech.util.JwtUtil;

@RestController
public class AuthTokenController {
    
    @Autowired
    private AuthTokenService authTokenService;
    
    @PostMapping("/api/{boardType}/{postId}/validate-password")
    public ResponseEntity<?> validatePassword(
            @PathVariable String boardType,
            @PathVariable int postId,
            @RequestBody PostPasswordRequest request) throws Exception {
        boolean isValid = authTokenService.validatePassword(boardType, postId, request.getPassword());
        
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("비밀번호가 틀립니다."));
        }
        
        String token = JwtUtil.generateToken(boardType, postId);
        return ResponseEntity.ok(new PostPasswordResponse(token));
    }
    
    
}
