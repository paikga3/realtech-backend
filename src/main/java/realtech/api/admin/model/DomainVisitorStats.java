package realtech.api.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DomainVisitorStats {
    private int rank; // 순위
    private String domain; // 접속 도메인
    private long visitorCount; // 접속자 수
    private double percentage; // 비율(%)
}
