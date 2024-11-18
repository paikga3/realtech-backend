package realtech.api.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import realtech.api.admin.model.DomainVisitorStatsResponse;
import realtech.api.admin.model.FetchVisitorAggregationParams;
import realtech.api.admin.model.FetchVisitorSearchParams;
import realtech.api.admin.service.AccessLogService;
import realtech.api.common.model.PagedResponse;
import realtech.db.entity.AccessLog;

@RestController
public class AccessLogController {
    
    @Autowired
    private AccessLogService accessLogService;
    
    @GetMapping("/api/admin/visitor-search")
    public PagedResponse<AccessLog> fetchVisitorSearch(FetchVisitorSearchParams rq) {
        return accessLogService.fetchVisitorSearch(rq);
    }
    
    
    @GetMapping("/api/admin/visitor-aggregation/visitor-list")
    public PagedResponse<AccessLog> fetchVisitorAggregation(FetchVisitorAggregationParams rq) {
        return accessLogService.fetchVisitorAggregation(rq);
    }
    
    @GetMapping("/api/admin/visitor-aggregation/domains")
    public DomainVisitorStatsResponse fetchDomainVisitorStats(FetchVisitorAggregationParams rq) {
        return accessLogService.fetchDomainVisitorStats(rq);
    }
}
