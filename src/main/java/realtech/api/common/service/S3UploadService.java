package realtech.api.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3UploadService {
    @Autowired
    private S3Client s3Client;


    public String uploadFile(MultipartFile file, String folderPath, String bucketName) {
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
}
