package realtech.api.front.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import realtech.api.front.model.CommentListResponse;
import realtech.api.front.service.CommentService;
import realtech.util.AppUtil;

@RestController
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    @GetMapping("/api/comment/list")
    public CommentListResponse getComments(@RequestParam("refTable") String refTable, @RequestParam("refId") int refId) throws Exception {

        CommentListResponse response = new CommentListResponse();
        response.setComments(commentService.getComments(refTable, refId));
        response.setTotalComments(AppUtil.countTotalComments(response.getComments()));
        
        return response;
    }
    
    
    
    
}
