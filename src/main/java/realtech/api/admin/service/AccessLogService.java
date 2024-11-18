package realtech.api.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import realtech.api.admin.model.DomainVisitorStats;
import realtech.api.admin.model.DomainVisitorStatsResponse;
import realtech.api.admin.model.FetchVisitorAggregationParams;
import realtech.api.admin.model.FetchVisitorSearchParams;
import realtech.api.common.model.PagedResponse;
import realtech.db.entity.AccessLog;

@Service
public class AccessLogService {
    
    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;
    
    public PagedResponse<AccessLog> fetchVisitorSearch(FetchVisitorSearchParams rq) {
        StringBuffer where = new StringBuffer();
        where.append(" from AccessLog t ");
        where.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(rq.getSearchKeyword())) {
            if ("ip".equals(rq.getSearchType())) {
                where.append(" and ipAddress like concat('%',:searchKeyword,'%') ");
            } else if ("referer".equals(rq.getSearchType())) {
                where.append(" and referer like concat('%',:searchKeyword,'%') ");
            }
        }
        where.append(" order by t.id desc ");
        StringBuffer countSelect = new StringBuffer(" select count(t) ")
                .append(where);
        StringBuffer bodySelect = new StringBuffer(" SELECT t ")
                .append(where);
        TypedQuery<Long> countQuery = entityManager.createQuery(countSelect.toString(), Long.class);
        if (StringUtils.isNotEmpty(rq.getSearchKeyword())) {
            countQuery.setParameter("searchKeyword", rq.getSearchKeyword());
        }
        
        int total = countQuery.getSingleResult().intValue();

        TypedQuery<AccessLog> listQuery = entityManager.createQuery(bodySelect.toString(), AccessLog.class);
        if (StringUtils.isNotEmpty(rq.getSearchKeyword())) {
            listQuery.setParameter("searchKeyword", rq.getSearchKeyword());
        }
        
        int start = (rq.getPage()-1) * rq.getPageSize();
        listQuery.setFirstResult(start); // 0부터 시작
        listQuery.setMaxResults(rq.getPageSize());
        
        PagedResponse<AccessLog> pr = new PagedResponse<>();
        pr.setData(listQuery.getResultList());
        pr.setTotal(total);
        pr.setCurrentPage(rq.getPage());
        pr.setPageSize(rq.getPageSize());
        
        return pr;
    }
    
    
    public PagedResponse<AccessLog> fetchVisitorAggregation(FetchVisitorAggregationParams rq) {
        StringBuffer where = new StringBuffer();
        where.append(" from AccessLog t ");
        where.append(" where 1=1 ");
        where.append(" and substr(t.accessTime,1,8) between :startDate and :endDate ");
        where.append(" order by t.id desc ");
        StringBuffer countSelect = new StringBuffer(" select count(t) ")
                .append(where);
        StringBuffer bodySelect = new StringBuffer(" SELECT t ")
                .append(where);
        TypedQuery<Long> countQuery = entityManager.createQuery(countSelect.toString(), Long.class);
        countQuery.setParameter("startDate", rq.getStartDate());
        countQuery.setParameter("endDate", rq.getEndDate());
        
        int total = countQuery.getSingleResult().intValue();

        TypedQuery<AccessLog> listQuery = entityManager.createQuery(bodySelect.toString(), AccessLog.class);
        listQuery.setParameter("startDate", rq.getStartDate());
        listQuery.setParameter("endDate", rq.getEndDate());
        
        // page가 0일 경우 전체조회
        if (rq.getPage() != 0) {
            int start = (rq.getPage()-1) * rq.getPageSize();
            listQuery.setFirstResult(start); // 0부터 시작
            listQuery.setMaxResults(rq.getPageSize());
        }
        
        
        PagedResponse<AccessLog> pr = new PagedResponse<>();
        pr.setData(listQuery.getResultList());
        pr.setTotal(total);
        pr.setCurrentPage(rq.getPage());
        pr.setPageSize(rq.getPageSize());
        
        return pr;
    }
    
    
    public DomainVisitorStatsResponse fetchDomainVisitorStats(FetchVisitorAggregationParams rq) {
        return processAccessLogs(fetchVisitorAggregation(rq).getData());
    }
    
    
    public DomainVisitorStatsResponse processAccessLogs(List<AccessLog> accessLogs) {
        DomainVisitorStatsResponse rs = new DomainVisitorStatsResponse();
        
        // Step 1: Extract domains from referer
        Map<String, Long> domainVisitorCount = accessLogs.stream()
                .map(log -> extractDomain(log.getReferer()))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(domain -> domain, Collectors.counting()));

        // Step 2: Calculate total visitors
        long totalVisitors = domainVisitorCount.values().stream().mapToLong(Long::longValue).sum();

        // Step 3: Create a sorted list without toList()
        List<Map.Entry<String, Long>> sortedEntries = new ArrayList<>(domainVisitorCount.entrySet());
        sortedEntries.sort((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())); // Sort by visitor count descending

        // Step 4: Generate DomainStats with shared rank logic
        List<DomainVisitorStats> domainStatsList = new ArrayList<>();
        int rank = 1;
        int previousRank = 0;
        long previousCount = -1;
        int total = 0;
        for (Map.Entry<String, Long> entry : sortedEntries) {
            String domain = entry.getKey();
            long visitorCount = entry.getValue();
            double percentage = ((double) visitorCount / totalVisitors) * 100;
            total += visitorCount;
            if (visitorCount != previousCount) {
                previousRank = rank;
            }

            domainStatsList.add(new DomainVisitorStats(previousRank, domain, visitorCount, Math.round(percentage * 10) / 10.0));

            previousCount = visitorCount;
            rank++;
        }
        rs.setData(domainStatsList);
        rs.setTotal(total);
        return rs;
    }

    private String extractDomain(String referer) {
        if (referer == null || !referer.contains("//")) {
            return null;
        }
        // Remove protocol
        String withoutProtocol = referer.substring(referer.indexOf("//") + 2);
        // Extract domain part before '/'
        int endIndex = withoutProtocol.indexOf("/");
        return (endIndex != -1) ? withoutProtocol.substring(0, endIndex) : withoutProtocol;
    }
    
    
}
