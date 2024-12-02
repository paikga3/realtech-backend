package realtech.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import realtech.api.front.model.CommentDetail;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

public class AppUtil {
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
    
    public static String formatToCompactDateTime(String inputDate) {
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
     * yyyyMMdd 형식의 문자열을 yyyy-MM-dd 형식의 문자열로 변환합니다.
     * 
     * @param inputDate 변환할 입력 날짜 (yyyyMMdd 형식)
     * @return 변환된 날짜 문자열 (yyyy-MM-dd 형식)
     * @throws IllegalArgumentException 입력 형식이 잘못된 경우
     */
    public static String convertToDashedFormat(String inputDate) {
        if (inputDate == null || inputDate.length() != 8) {
            throw new IllegalArgumentException("입력 날짜 형식이 잘못되었습니다. yyyyMMdd 형식이어야 합니다.");
        }

        try {
            // 입력 형식 지정
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            // 출력 형식 지정
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            // 입력 날짜 문자열을 LocalDate로 변환
            LocalDate date = LocalDate.parse(inputDate, inputFormatter);
            
            // 변환된 날짜를 출력 형식으로 변환
            return date.format(outputFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("유효하지 않은 날짜 형식입니다: " + inputDate, e);
        }
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
    
    // Helper 메서드: IPv4-mapped IPv6 주소 처리
    private static String extractIPv4FromIPv6(String ip) {
        if (ip != null && ip.startsWith("::ffff:")) {
            return ip.substring(7); // "::ffff:" 접두사를 제거하여 IPv4 주소 반환
        }
        return ip;
    }
    
    public static String getClientIpFromRequest(HttpServletRequest request) {

        // X-Forwarded-For 헤더에서 IP 가져오기
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // X-Forwarded-For에 여러 IP가 있는 경우 첫 번째 IP 반환
            ip = ip.split(",")[0].trim();
            return extractIPv4FromIPv6(ip);
        }

        // X-Real-IP 헤더에서 IP 가져오기
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return extractIPv4FromIPv6(ip);
        }

        // 프록시 정보가 없는 경우 getRemoteAddr 사용
        String remoteAddr = request.getRemoteAddr();
        return extractIPv4FromIPv6(remoteAddr);
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
    
    public static BigDecimal getFileSizeInKB(HeadObjectResponse head) {
        if (head == null) {
            return BigDecimal.ZERO; // 파일이 없거나 비어 있으면 0 반환
        }

        // 바이트 단위의 파일 크기 가져오기
        long sizeInBytes = head.contentLength();

        // KB로 변환 (1KB = 1024 Bytes)
        BigDecimal sizeInKB = BigDecimal.valueOf(sizeInBytes)
                .divide(BigDecimal.valueOf(1024), 2, RoundingMode.HALF_UP); // 소수점 2자리 반올림

        return sizeInKB;
    }
    

    /**
     * 지원되는 이미지 포맷 확인 메서드
     *
     * @param format 파일 확장자
     * @return 지원 여부
     */
    public static boolean isSupportedFormat(String format) {
        return format.equals("jpg") || format.equals("jpeg") || format.equals("png") || format.equals("gif") || format.equals("bmp");
    }
    
    
 // 확장자별 Content-Type 매핑
    private static final Map<String, String> EXTENSION_TO_CONTENT_TYPE = new HashMap<>();

    static {
        EXTENSION_TO_CONTENT_TYPE.put("jpg", "image/jpeg");
        EXTENSION_TO_CONTENT_TYPE.put("jpeg", "image/jpeg");
        EXTENSION_TO_CONTENT_TYPE.put("png", "image/png");
        EXTENSION_TO_CONTENT_TYPE.put("gif", "image/gif");
        EXTENSION_TO_CONTENT_TYPE.put("bmp", "image/bmp");
        EXTENSION_TO_CONTENT_TYPE.put("tiff", "image/tiff");
        EXTENSION_TO_CONTENT_TYPE.put("webp", "image/webp");
        EXTENSION_TO_CONTENT_TYPE.put("svg", "image/svg+xml");
    }

    /**
     * 확장자를 기반으로 Content-Type 반환
     *
     * @param extension 파일 확장자 (예: "jpg", "png")
     * @return Content-Type 문자열 (예: "image/jpeg"), 지원되지 않는 확장자의 경우 기본값 반환
     */
    public static String getContentType(String extension) {
        if (extension == null || extension.isEmpty()) {
            return "application/octet-stream"; // 기본 Content-Type
        }

        // 확장자를 소문자로 변환 후 매핑 확인
        return EXTENSION_TO_CONTENT_TYPE.getOrDefault(extension.toLowerCase(), "application/octet-stream");
    }
    
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String password, byte[] salt) throws Exception {
        int iterations = 10000;
        int keyLength = 256;
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = keyFactory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }
    
    // 전체 댓글 수를 계산하는 메서드
    public static int countTotalComments(List<CommentDetail> comments) {
        int total = 0;
        for (CommentDetail comment : comments) {
            total += countCommentAndReplies(comment);
        }
        return total;
    }

    // 단일 댓글과 그 자식 댓글의 수를 재귀적으로 계산
    private static int countCommentAndReplies(CommentDetail comment) {
        int count = comment.getIsDeleted() == 0 ? 1 : 0; // 현재 댓글 포함
        if (comment.getReplies() != null) {
            for (CommentDetail reply : comment.getReplies()) {
                count += countCommentAndReplies(reply); // 자식 댓글의 수 누적
            }
        }
        return count;
    }
    
    // 전체 댓글 수를 계산하는 메서드
    public static int countAdminComments(List<CommentDetail> comments) {
        int total = 0;
        for (CommentDetail comment : comments) {
            total += countAdminCommentAndReplies(comment);
        }
        return total;
    }
    
    // 단일 댓글과 그 자식 댓글의 수를 재귀적으로 계산
    private static int countAdminCommentAndReplies(CommentDetail comment) {
        int count = comment.getIsDeleted() == 0 && comment.getIsAdmin() == 1 ? 1 : 0; // 현재 댓글 포함

        if (comment.getReplies() != null) {
            for (CommentDetail reply : comment.getReplies()) {
                count += countAdminCommentAndReplies(reply); // 자식 댓글의 수 누적
            }
        }
        return count;
    }
}
