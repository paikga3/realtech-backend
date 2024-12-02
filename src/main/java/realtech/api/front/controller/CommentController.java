package realtech.api.front.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import realtech.api.front.model.BaseCommentRequest;
import realtech.api.front.model.CommentListResponse;
import realtech.api.front.model.CreateCommentParams;
import realtech.api.front.model.UpdateCommentParams;
import realtech.api.front.service.CommentService;
import realtech.util.AesEncryptionUtil;
import realtech.util.AppUtil;
import realtech.util.JwtValidator;
import realtech.util.SecurityUtil;

@RestController
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    @GetMapping("/api/comment")
    public CommentListResponse getComments(BaseCommentRequest params, HttpServletRequest request) throws Exception {
        if (!SecurityUtil.isAdmin() && "reservation_inquiry".equals(params.getRefTable())) {
            // Authorization 헤더 처리
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Missing or invalid Authorization header");
            }

            String token = AesEncryptionUtil.decode(authorizationHeader.substring(7));
            JwtValidator.validatePostToken(token, params.getRefTable(), params.getRefId());
        }
        
        
        CommentListResponse response = new CommentListResponse();
        response.setComments(commentService.getComments(params.getRefTable(), params.getRefId()));
        response.setTotalComments(AppUtil.countTotalComments(response.getComments()));
        
        return response;
    }
    
    @PostMapping("/api/comment")
    public void addComment(@RequestBody CreateCommentParams params, HttpServletRequest request) throws Exception {
        commentService.addComment(params, request);
    }
    
    @PutMapping("/api/comment")
    public void updateComment(@RequestBody UpdateCommentParams params, HttpServletRequest request) throws Exception {
        commentService.updateComment(params, request);
    }
    
    @DeleteMapping("/api/comment")
    public void deleteComment(@RequestBody UpdateCommentParams params, HttpServletRequest request) throws Exception {
        commentService.deleteComment(params, request);
    }
}
