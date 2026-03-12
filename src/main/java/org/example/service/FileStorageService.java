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

    /**
     * Загружает файл в Yandex Cloud Object Storage
     * @param file Файл для загрузки
     * @param userId ID пользователя (для организации папок)
     * @return Публичный URL файла
     */
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
                .build(), RequestBody.fromInputStream(file.getInputStream(), file.getSize()));


        String url = endpoint + "/" + bucket + "/" + key;

        log.info(" File uploaded to Yandex Cloud: {}", url);
        return url;
    }

    /**
     * Удаляет файл из облака
     * @param fileUrl URL файла для удаления
     */
    public void deleteFile(String fileUrl) {
        try {
            String key = extractKeyFromUrl(fileUrl);
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
            log.info(" File deleted from Yandex Cloud: {}", fileUrl);
        } catch (Exception e) {
            log.error(" Failed to delete file: {}", fileUrl, e);
        }
    }


    private String extractKeyFromUrl(String url) {

        String bucketPath = "/" + bucket + "/";
        int startIndex = url.indexOf(bucketPath);
        if (startIndex == -1) {
            return url.substring(url.lastIndexOf("/") + 1);
        }
        return url.substring(startIndex + bucketPath.length());
    }
}