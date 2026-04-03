package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    private final S3Client s3Client;

    @Value("${cloud.storage.bucket}")
    private String bucket;

    @Value("${cloud.storage.endpoint}")
    private String endpoint;

    public String uploadFile(MultipartFile file, String userId) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";

        String key = "users/" + userId + "/" + UUID.randomUUID() + extension;

        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build(), RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        String url = endpoint + "/" + bucket + "/" + key;

        log.info("File uploaded to Yandex Cloud: {}", url);
        return url;
    }

    public void deleteFile(String fileUrl) {
        try {
            String key = extractKeyFromUrl(fileUrl);
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
            log.info("File deleted from Yandex Cloud: {}", fileUrl);
        } catch (Exception e) {
            log.error("Failed to delete file: {}", fileUrl, e);
        }
    }

    public InputStream getFileStream(String fileUrl) {
        String key = extractKeyFromUrl(fileUrl);
        log.debug("Getting file stream: bucket={}, key={}", bucket, key);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        return s3Client.getObject(getObjectRequest);
    }

    public String getFileContentType(String fileUrl) {
        try {
            String key = extractKeyFromUrl(fileUrl);

            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            String contentType = s3Client.headObject(headRequest).contentType();
            return contentType != null ? contentType : "application/octet-stream";

        } catch (Exception e) {
            log.warn("Failed to get content type for: {}", fileUrl);
            return "application/octet-stream";
        }
    }

    public String extractKeyFromUrl(String url) {
        try {
            String bucketPath = "/" + bucket + "/";
            int startIndex = url.indexOf(bucketPath);
            if (startIndex == -1) {
                return url.substring(url.lastIndexOf("/") + 1);
            }
            return url.substring(startIndex + bucketPath.length());
        } catch (Exception e) {
            log.warn("Failed to parse file URL: {}", url);
            return url.replaceAll("^https://[^/]+/[^/]+/", "");
        }
    }

    public boolean fileExists(String fileUrl) {
        try {
            String key = extractKeyFromUrl(fileUrl);
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (Exception e) {
            log.warn("Error checking file existence: {}", e.getMessage());
            return false;
        }
    }
}