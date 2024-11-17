package realtech.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppUtils {
    public static boolean isNewPost(String createdAt) {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        
        // createdAt 문자열을 LocalDateTime으로 변환
        LocalDateTime createdDateTime = LocalDateTime.parse(createdAt, FORMATTER);

        // 현재 시간
        LocalDateTime now = LocalDateTime.now();

        // createdAt과 현재 시간의 차이를 계산
        Duration duration = Duration.between(createdDateTime, now);

        // 10분(600초) 이내인지 확인
        return duration.toMinutes() < 10;
    }
}
