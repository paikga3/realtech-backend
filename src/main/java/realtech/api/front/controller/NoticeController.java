package realtech.api.front.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import realtech.api.common.model.PagedResponse;
import realtech.api.front.model.CreateNoticeParams;
import realtech.api.front.model.FetchNoticesParams;
import realtech.api.front.model.NoticeDetail;
import realtech.api.front.model.NoticePost;
import realtech.api.front.service.NoticeService;

@RestController
public class NoticeController {
    
    @Autowired
    private NoticeService noticeService;
    
    
    @GetMapping("/api/notice-post")
    public PagedResponse<NoticePost> fetchNotices(FetchNoticesParams rq) {
        return noticeService.fetchNotices(rq);
    }
    
    @GetMapping("/api/notice-post/{id}")
    public NoticeDetail getNoticeDetail(
            @PathVariable("id") int id, 
            @RequestParam(value = "edit", defaultValue = "view") String purpose) throws Exception {
        boolean isUpdateViewCount = purpose.equals("view");
        return noticeService.getNoticeDetail(id, isUpdateViewCount);
    }
    
    // 게시물 등록 API
    @PostMapping("/api/admin/notice-post")
    public void createNotice(@ModelAttribute CreateNoticeParams params, HttpServletRequest request) throws Exception {
        noticeService.createNotice(params, request);
    }
    
    // 게시물 수정 API
    @PutMapping("/api/admin/notice-post/{id}")
    public void updateNotice(@PathVariable("id") int id, @ModelAttribute CreateNoticeParams params, HttpServletRequest request) throws Exception {
        noticeService.updateNotice(id, params, request);
    }
    
    // 게시물 삭제 API
    @DeleteMapping("/api/admin/notice-post/{id}")
    public void deleteNotice(@PathVariable("id") int id) {
        noticeService.deleteNotice(id);
    }
    
}
