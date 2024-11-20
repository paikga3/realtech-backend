package realtech.api.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import realtech.api.admin.model.CommonStatsResponse;
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
    public CommonStatsResponse fetchDomainVisitorStats(FetchVisitorAggregationParams rq) {
        return accessLogService.fetchDomainVisitorStats(rq);
    }
    
    
    @GetMapping("/api/admin/visitor-aggregation/browser-stats")
    public CommonStatsResponse fetchBrowserStats(FetchVisitorAggregationParams rq) {
        return accessLogService.fetchBrowserStats(rq);
    }
    
    @GetMapping("/api/admin/visitor-aggregation/os-stats")
    public CommonStatsResponse fetchOperatingSystemStats(FetchVisitorAggregationParams rq) {
        return accessLogService.fetchOperatingSystemStats(rq);
    }
    
    @GetMapping("/api/admin/visitor-aggregation/device-stats")
    public CommonStatsResponse fetchDeviceStats(FetchVisitorAggregationParams rq) {
        return accessLogService.fetchDeviceStats(rq);
    }
    
    @GetMapping("/api/admin/visitor-aggregation/hourly-stats")
    public CommonStatsResponse fetchHourlyStats(FetchVisitorAggregationParams rq) {
        return accessLogService.fetchHourlyStats(rq);
    }
    
    @GetMapping("/api/admin/visitor-aggregation/weekly-stats")
    public CommonStatsResponse fetchWeeklyStats(FetchVisitorAggregationParams rq) {
        return accessLogService.fetchWeeklyStats(rq);
    }
    
    @GetMapping("/api/admin/visitor-aggregation/daily-stats")
    public CommonStatsResponse fetchDailyStats(FetchVisitorAggregationParams rq) {
        return accessLogService.fetchDailyStats(rq);
    }
    
    @GetMapping("/api/admin/visitor-aggregation/monthly-stats")
    public CommonStatsResponse fetchMonthlyStats(FetchVisitorAggregationParams rq) {
        return accessLogService.fetchMonthlyStats(rq);
    }
    
    @GetMapping("/api/admin/visitor-aggregation/yearly-stats")
    public CommonStatsResponse fetchYearlyStats(FetchVisitorAggregationParams rq) {
        return accessLogService.fetchYearlyStats(rq);
    }
    
}
