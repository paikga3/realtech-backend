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
import realtech.util.AppUtil;

@RestController
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    @GetMapping("/api/comment")
    public CommentListResponse getComments(BaseCommentRequest request) {

        CommentListResponse response = new CommentListResponse();
        response.setComments(commentService.getComments(request.getRefTable(), request.getRefId()));
        response.setTotalComments(AppUtil.countTotalComments(response.getComments()));
        
        return response;
    }
    
    @PostMapping("/api/comment")
    public void addComment(@RequestBody CreateCommentParams params, HttpServletRequest request) {
        commentService.addComment(params, request);
    }
    
    @PutMapping("/api/comment")
    public void updateComment(@RequestBody UpdateCommentParams params, HttpServletRequest request) {
        commentService.updateComment(params, request);
    }
    
    @DeleteMapping("/api/comment")
    public void deleteComment(@RequestBody UpdateCommentParams params, HttpServletRequest request) {
        commentService.deleteComment(params, request);
    }
}
