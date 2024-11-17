package realtech.api.front.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import realtech.api.common.model.PagedResponse;
import realtech.api.front.model.FetchNoticesParams;
import realtech.api.front.model.Notice;
import realtech.api.front.service.NoticeService;

@RestController
public class NoticeController {
    
    @Autowired
    private NoticeService noticeService;
    
    
    @GetMapping("/api/notices")
    public PagedResponse<Notice> fetchNotices(FetchNoticesParams rq) {
        return noticeService.fetchNotices(rq);
    }
    
}
