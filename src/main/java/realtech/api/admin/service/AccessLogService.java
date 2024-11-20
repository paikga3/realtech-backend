package realtech.api.admin.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import realtech.api.admin.model.CommonStats;
import realtech.api.admin.model.CommonStatsResponse;
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
    
    
    public CommonStatsResponse fetchDomainVisitorStats(FetchVisitorAggregationParams rq) {
        List<AccessLog> accessLogs = fetchVisitorAggregation(rq).getData();
        Comparator<Map.Entry<String, Long>> byVisitorCountDesc =
                (e1, e2) -> Long.compare(e2.getValue(), e1.getValue());
        return processStats(
                accessLogs,
            log -> extractDomain(log.getReferer()), // Domain 추출
            (rank, entry) -> {
                double percentage = ((double) entry.getValue() / accessLogs.size()) * 100;
                return new CommonStats(rank, entry.getKey(), entry.getValue(), Math.round(percentage * 10) / 10.0);
            },
            byVisitorCountDesc
        );
    }

    public CommonStatsResponse fetchBrowserStats(FetchVisitorAggregationParams rq) {
        List<AccessLog> accessLogs = fetchVisitorAggregation(rq).getData();
        Comparator<Map.Entry<String, Long>> byVisitorCountDesc =
                (e1, e2) -> Long.compare(e2.getValue(), e1.getValue());
        return processStats(
                accessLogs,
                AccessLog::getBrowser, // Browser 추출
                (rank, entry) -> {
                    double percentage = ((double) entry.getValue() / accessLogs.size()) * 100;
                    return new CommonStats(rank, entry.getKey(), entry.getValue(), Math.round(percentage * 10) / 10.0);
                },
                byVisitorCountDesc
            );
    }
    

    
    public CommonStatsResponse fetchOperatingSystemStats(FetchVisitorAggregationParams rq) {
        List<AccessLog> accessLogs = fetchVisitorAggregation(rq).getData();
        Comparator<Map.Entry<String, Long>> byVisitorCountDesc =
                (e1, e2) -> Long.compare(e2.getValue(), e1.getValue());
        return processStats(
                accessLogs,
                AccessLog::getOs, // OS 추출
                (rank, entry) -> {
                    double percentage = ((double) entry.getValue() / accessLogs.size()) * 100;
                    return new CommonStats(rank, entry.getKey(), entry.getValue(), Math.round(percentage * 10) / 10.0);
                },
                byVisitorCountDesc
            );
    }
    
    public CommonStatsResponse fetchDeviceStats(FetchVisitorAggregationParams rq) {
        List<AccessLog> accessLogs = fetchVisitorAggregation(rq).getData();
        Comparator<Map.Entry<String, Long>> byVisitorCountDesc =
                (e1, e2) -> Long.compare(e2.getValue(), e1.getValue());
        return processStats(
                accessLogs,
                AccessLog::getDevice, // device 추출
                (rank, entry) -> {
                    double percentage = ((double) entry.getValue() / accessLogs.size()) * 100;
                    return new CommonStats(rank, entry.getKey(), entry.getValue(), Math.round(percentage * 10) / 10.0);
                },
                byVisitorCountDesc
            );
    }
    
    public CommonStatsResponse fetchHourlyStats(FetchVisitorAggregationParams rq) {
        List<AccessLog> accessLogs = fetchVisitorAggregation(rq).getData();
        Comparator<Map.Entry<String, Long>> byHourlyAsc =
                (e1, e2) -> e1.getKey().compareTo(e2.getKey());
        CommonStatsResponse rs = processStats(
                accessLogs,
                a -> {
                    return a.getAccessTime().substring(8, 10);
                }, // device 추출
                (rank, entry) -> {
                    double percentage = ((double) entry.getValue() / accessLogs.size()) * 100;
                    return new CommonStats(rank, entry.getKey(), entry.getValue(), Math.round(percentage * 10) / 10.0);
                },
                byHourlyAsc
            );
        // 모든 시간대 (00~23) 생성
        List<String> allHours = IntStream.range(0, 24)
                                         .mapToObj(hour -> String.format("%02d", hour))
                                         .collect(Collectors.toList());

        // 시간대 매핑
        Map<String, CommonStats> visitorDataMap = rs.getData().stream()
                                                                 .collect(Collectors.toMap(CommonStats::getCategory, v -> v));
        
        // 누락된 시간대를 채워 데이터 생성
        List<CommonStats> completeData = allHours.stream()
                                                 .map(hour -> visitorDataMap.getOrDefault(hour, new CommonStats(0, hour, 0, 0)))
                                                 .collect(Collectors.toList());
        
        rs.setData(completeData);
        
        return rs;
    }
    
    public CommonStatsResponse fetchWeeklyStats(FetchVisitorAggregationParams rq) {
        List<AccessLog> accessLogs = fetchVisitorAggregation(rq).getData();
        Comparator<Map.Entry<String, Long>> byWeeklyAsc =
                (e1, e2) -> {
                    // 요일 순서 맵핑
                    List<String> dayOrder = Arrays.asList("월", "화", "수", "목", "금", "토", "일");
                    return dayOrder.indexOf(e1.getKey()) - dayOrder.indexOf(e2.getKey());
                };
        CommonStatsResponse rs = processStats(
                accessLogs,
                a -> {
                    // yyyyMMddHHmmss 형식의 DateTimeFormatter 생성
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    // 문자열을 LocalDateTime으로 변환
                    LocalDateTime localDateTime = LocalDateTime.parse(a.getAccessTime(), formatter);
                    // 요일 가져오기
                    DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
                    // 요일을 한국어로 변환 (월, 화, 수, ...)
                    return dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.KOREAN);
                }, // device 추출
                (rank, entry) -> {
                    double percentage = ((double) entry.getValue() / accessLogs.size()) * 100;
                    return new CommonStats(rank, entry.getKey(), entry.getValue(), Math.round(percentage * 10) / 10.0);
                },
                byWeeklyAsc
            );
        // 모든 요일 (월~일) 생성
        List<String> allWeeks = Arrays.asList("월", "화", "수", "목", "금", "토", "일");

        // 요일대 매핑
        Map<String, CommonStats> visitorDataMap = rs.getData().stream()
                                                                 .collect(Collectors.toMap(CommonStats::getCategory, v -> v));
        
        // 누락된 요일을 채워 데이터 생성
        List<CommonStats> completeData = allWeeks.stream()
                                                 .map(week -> visitorDataMap.getOrDefault(week, new CommonStats(0, week, 0, 0)))
                                                 .collect(Collectors.toList());
        
        rs.setData(completeData);
        
        return rs;
    }
    
    public CommonStatsResponse fetchDailyStats(FetchVisitorAggregationParams rq) {
        List<AccessLog> accessLogs = fetchVisitorAggregation(rq).getData();
        Comparator<Map.Entry<String, Long>> byWeeklyAsc =
                (e1, e2) -> {
                    return e1.getKey().compareTo(e2.getKey());
                };
        CommonStatsResponse rs = processStats(
                accessLogs,
                a -> {
                    // 입력 형식 (yyyyMMddHHmmss)
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    // 출력 형식 (yyyy-MM-dd)
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    // 입력 문자열을 LocalDateTime 객체로 변환
                    LocalDateTime localDateTime = LocalDateTime.parse(a.getAccessTime(), inputFormatter);
                    // 원하는 형식으로 변환하여 문자열 반환
                    return localDateTime.format(outputFormatter);
                }, // device 추출
                (rank, entry) -> {
                    double percentage = ((double) entry.getValue() / accessLogs.size()) * 100;
                    return new CommonStats(rank, entry.getKey(), entry.getValue(), Math.round(percentage * 10) / 10.0);
                },
                byWeeklyAsc
            );
        

        // 시작일과 종료일 LocalDate 변환
        LocalDate start = LocalDate.parse(rq.getStartDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate end = LocalDate.parse(rq.getEndDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 기존 데이터를 Map으로 변환 (key: 날짜, value: 데이터)
        Map<String, CommonStats> dataMap = new HashMap<>();
        for (CommonStats data : rs.getData()) {
            dataMap.put(data.getCategory(), data);
        }

        // 모든 날짜를 포함한 리스트 생성
        List<CommonStats> completeData = new ArrayList<>();
        LocalDate current = start;
        while (!current.isAfter(end)) {
            String currentDate = current.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // Map에서 데이터가 없으면 기본값 추가
            completeData.add(dataMap.getOrDefault(currentDate, 
                new CommonStats(0, currentDate, 0, 0.0)));
            current = current.plusDays(1);
        }
        
        rs.setData(completeData);
        
        return rs;
    }
    
    public CommonStatsResponse fetchMonthlyStats(FetchVisitorAggregationParams rq) {
        List<AccessLog> accessLogs = fetchVisitorAggregation(rq).getData();
        Comparator<Map.Entry<String, Long>> byWeeklyAsc =
                (e1, e2) -> {
                    return e1.getKey().compareTo(e2.getKey());
                };
        CommonStatsResponse rs = processStats(
                accessLogs,
                a -> {
                    // 입력 형식 (yyyyMMddHHmmss)
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    // 출력 형식 (yyyy-MM)
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

                    // 입력 문자열을 LocalDateTime 객체로 변환
                    LocalDateTime localDateTime = LocalDateTime.parse(a.getAccessTime(), inputFormatter);
                    // 원하는 형식으로 변환하여 문자열 반환
                    return localDateTime.format(outputFormatter);
                }, // device 추출
                (rank, entry) -> {
                    double percentage = ((double) entry.getValue() / accessLogs.size()) * 100;
                    return new CommonStats(rank, entry.getKey(), entry.getValue(), Math.round(percentage * 10) / 10.0);
                },
                byWeeklyAsc
            );
        

        // 시작일과 종료일 LocalDate 변환
        LocalDate start = LocalDate.parse(rq.getStartDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate end = LocalDate.parse(rq.getEndDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 기존 데이터를 Map으로 변환 (key: 날짜, value: 데이터)
        Map<String, CommonStats> dataMap = new HashMap<>();
        for (CommonStats data : rs.getData()) {
            dataMap.put(data.getCategory(), data);
        }

        // 모든 날짜를 포함한 리스트 생성
        List<CommonStats> completeData = new ArrayList<>();
        LocalDate current = start;
        while (!current.isAfter(end)) {
            String currentDate = current.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            // Map에서 데이터가 없으면 기본값 추가
            completeData.add(dataMap.getOrDefault(currentDate, 
                new CommonStats(0, currentDate, 0, 0.0)));
            current = current.plusMonths(1);
        }
        
        rs.setData(completeData);
        
        return rs;
    }
    
    public CommonStatsResponse fetchYearlyStats(FetchVisitorAggregationParams rq) {
        List<AccessLog> accessLogs = fetchVisitorAggregation(rq).getData();
        Comparator<Map.Entry<String, Long>> byWeeklyAsc =
                (e1, e2) -> {
                    return e1.getKey().compareTo(e2.getKey());
                };
        CommonStatsResponse rs = processStats(
                accessLogs,
                a -> {
                    // 입력 형식 (yyyyMMddHHmmss)
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    // 출력 형식 (yyyy-MM-dd)
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy");

                    // 입력 문자열을 LocalDateTime 객체로 변환
                    LocalDateTime localDateTime = LocalDateTime.parse(a.getAccessTime(), inputFormatter);
                    // 원하는 형식으로 변환하여 문자열 반환
                    return localDateTime.format(outputFormatter);
                }, // device 추출
                (rank, entry) -> {
                    double percentage = ((double) entry.getValue() / accessLogs.size()) * 100;
                    return new CommonStats(rank, entry.getKey(), entry.getValue(), Math.round(percentage * 10) / 10.0);
                },
                byWeeklyAsc
            );
        

        // 시작일과 종료일 LocalDate 변환
        LocalDate start = LocalDate.parse(rq.getStartDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate end = LocalDate.parse(rq.getEndDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 기존 데이터를 Map으로 변환 (key: 날짜, value: 데이터)
        Map<String, CommonStats> dataMap = new HashMap<>();
        for (CommonStats data : rs.getData()) {
            dataMap.put(data.getCategory(), data);
        }

        // 모든 날짜를 포함한 리스트 생성
        List<CommonStats> completeData = new ArrayList<>();
        LocalDate current = start;
        while (!current.isAfter(end)) {
            String currentDate = current.format(DateTimeFormatter.ofPattern("yyyy"));
            // Map에서 데이터가 없으면 기본값 추가
            completeData.add(dataMap.getOrDefault(currentDate, 
                new CommonStats(0, currentDate, 0, 0.0)));
            current = current.plusYears(1);
        }
        
        rs.setData(completeData);
        
        return rs;
    }
    
    
    public CommonStatsResponse processStats(
            List<AccessLog> accessLogs,
            Function<AccessLog, String> extractor,
            BiFunction<Integer, Map.Entry<String, Long>, CommonStats> mapper,
            Comparator<Map.Entry<String, Long>> comparator // 정렬 기준을 Map.Entry에 적용
    ) {
        CommonStatsResponse response = new CommonStatsResponse();

        // Step 1: 데이터를 추출
        Map<String, Long> visitorCountMap = accessLogs.stream()
                .map(extractor)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Step 2: 전체 방문자 수 계산
        long totalVisitors = visitorCountMap.values().stream().mapToLong(Long::longValue).sum();

        // Step 3: 방문자 수 기준 내림차순 정렬
        List<Map.Entry<String, Long>> sortedEntries = new ArrayList<>(visitorCountMap.entrySet());
        sortedEntries.sort(comparator);
//        sortedEntries.sort((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()));

        // Step 4: 랭크 및 통계 데이터 생성
        List<CommonStats> statsList = new ArrayList<>();
        int rank = 1;
        int previousRank = 0;
        long previousCount = -1;

        for (Map.Entry<String, Long> entry : sortedEntries) {
            if (entry.getValue() != previousCount) {
                previousRank = rank;
            }

            statsList.add(mapper.apply(previousRank, entry));

            previousCount = entry.getValue();
            rank++;
        }

        response.setData(statsList);
        response.setTotal((int) totalVisitors);
        return response;
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
