package realtech.api.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CommonStats {
    private int rank; // 순위
    private String category; // 카테고리
    private long visitorCount; // 접속자 수
    private double percentage; // 비율(%)
}
