package realtech.api.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;
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
