package realtech.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

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
    
    public static String convertDateFormat(String inputDate) {
        // 입력 날짜 형식
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        // 출력 날짜 형식
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");

        // 입력 문자열을 LocalDateTime으로 파싱
        LocalDateTime dateTime = LocalDateTime.parse(inputDate, inputFormatter);
        // 원하는 형식으로 변환
        return dateTime.format(outputFormatter);
    }
    
    /**
     * 지정된 테이블명과 현재 날짜를 기반으로 경로 문자열을 생성하는 함수
     * @param tableName 테이블명
     * @return data/editor/테이블명/yyMM 형식의 문자열
     */
    public static String generateEditorPath(String tableName) {
        // 현재 날짜 가져오기
        LocalDate currentDate = LocalDate.now();

        // yyMM 형식 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMM");

        // 현재 날짜를 yyMM 형식으로 변환
        String dateString = currentDate.format(formatter);

        // 경로 생성 및 반환
        return String.format("data/editor/%s/%s", tableName, dateString);
    }
    
    public static String generateAttachmentPath(String tableName) {
        // 현재 날짜 가져오기
        LocalDate currentDate = LocalDate.now();

        // yyMM 형식 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMM");

        // 현재 날짜를 yyMM 형식으로 변환
        String dateString = currentDate.format(formatter);

        // 경로 생성 및 반환
        return String.format("data/file/%s/%s", tableName, dateString);
    }
    
    /**
     * MultipartFile로부터 파일 크기를 KB 단위로 변환하여 BigDecimal로 반환
     * 
     * @param file MultipartFile 객체
     * @return 파일 크기 (KB 단위, BigDecimal)
     */
    public static BigDecimal getFileSizeInKB(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return BigDecimal.ZERO; // 파일이 없거나 비어 있으면 0 반환
        }

        // 바이트 단위의 파일 크기 가져오기
        long sizeInBytes = file.getSize();

        // KB로 변환 (1KB = 1024 Bytes)
        BigDecimal sizeInKB = BigDecimal.valueOf(sizeInBytes)
                .divide(BigDecimal.valueOf(1024), 2, RoundingMode.HALF_UP); // 소수점 2자리 반올림

        return sizeInKB;
    }
    
    public static String getClientIpFromRequest(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // X-Forwarded-For에 여러 IP가 있는 경우 첫 번째 IP 반환
            return ip.split(",")[0];
        }

        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        // 프록시 정보가 없는 경우 getRemoteAddr 사용
        String remoteAddr = request.getRemoteAddr();

        // IPv4-mapped IPv6 주소인지 확인
        if (remoteAddr != null && remoteAddr.startsWith("::ffff:")) {
            return remoteAddr.substring(7); // "::ffff:" 접두사를 제거하여 IPv4 주소 반환
        }
        return remoteAddr; // 기본 반환
    }
    
    /**
     * 현재 날짜와 시간을 yyyyMMddHHmmss 형태로 반환
     * @return 현재 날짜와 시간 (yyyyMMddHHmmss)
     */
    public static String getCurrentDateTime() {
        // 현재 날짜와 시간 가져오기
        LocalDateTime now = LocalDateTime.now();

        // yyyyMMddHHmmss 형식의 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        // 포맷 적용 후 문자열 반환
        return now.format(formatter);
    }
    
    public static String extractPathUsingString(String url) {
        // "data/"의 시작 위치를 찾음
        int startIndex = url.indexOf("data/");

        // 시작 위치가 없으면 null 반환
        if (startIndex == -1) {
            return null;
        }

        // 시작 위치부터 문자열의 끝까지 추출
        return url.substring(startIndex);
    }
}
