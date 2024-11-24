package realtech.api.common.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import realtech.util.AppUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Log4j2
@Service
public class S3Service {
    @Autowired
    private S3Client s3Client;
    private String bucketName = "realtech-board";
    
    public String uploadResizedImageToS3(MultipartFile file, String folderPath) {
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IllegalArgumentException("파일의 확장자를 확인할 수 없습니다.");
        }

        // 파일 확장자에서 포맷 추출
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

        // 지원하지 않는 포맷에 대한 예외 처리
        if (!AppUtils.isSupportedFormat(fileExtension)) {
            throw new IllegalArgumentException("지원되지 않는 파일 포맷입니다: " + fileExtension);
        }
        
        // 고유한 파일 이름 생성
        String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
        String key = folderPath + "/" + uniqueFileName;

        try {
            // 리사이징된 이미지 생성
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                      .forceSize(298, 180)
                      .outputFormat(fileExtension)
                      .toOutputStream(outputStream);

            byte[] resizedImageBytes = outputStream.toByteArray();
            long resizedImageSize = resizedImageBytes.length;

            try (InputStream inputStream = new ByteArrayInputStream(resizedImageBytes)) {

                // S3에 파일 업로드
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(AppUtils.getContentType(fileExtension))
                        .build();

                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, resizedImageSize));

                // 업로드된 파일의 S3 URL 반환
                return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
            }
        } catch (IOException e) {
            log.error("Image resizing failed", e);
            throw new RuntimeException("Failed to resize image", e);
        } catch (Exception e) {
            log.error("Failed to upload file to S3", e);
            return "https://realtech-board.s3.ap-northeast-2.amazonaws.com/data/assets/free-icon-broken-image-13434972.png";
        }
    }
    
    
    public String uploadFile(MultipartFile file, String folderPath) {
        // 고유한 파일 이름 생성
        String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
        String key = folderPath + "/" + uniqueFileName; // 폴더 경로 포함
        try (InputStream inputStream = file.getInputStream()){
            // S3에 파일 업로드
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));

            // 업로드된 파일의 S3 URL 반환
            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID().toString() + extension;
    }
    
    public InputStream downloadFile(String s3Filename) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Filename)
                .build();

        return s3Client.getObject(getObjectRequest);
    }
    
    public HeadObjectResponse getHeader(String key) {
        // HeadObjectRequest 생성
        HeadObjectRequest request = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        // HeadObjectResponse를 통해 메타데이터 가져오기
        return s3Client.headObject(request);
    }
    
    
    public void deleteFile(String keyName) {
        // S3Client 생성
        try {
            // 삭제 요청 생성
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            // 파일 삭제 실행
            s3Client.deleteObject(deleteRequest);

            log.info("File deleted successfully from bucket: " + bucketName + ", key: " + keyName);
        } catch (S3Exception e) {
            log.info("Failed to delete file: " + e.awsErrorDetails().errorMessage());
        } catch (Exception e) {
            log.info("Unexpected error: " + e.getMessage());
        }
    }
    
}
