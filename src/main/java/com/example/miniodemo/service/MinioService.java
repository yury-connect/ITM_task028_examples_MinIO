package com.example.miniodemo.service;

import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.stream.StreamSupport;

//@Slf4j
@Service
public class MinioService {

    private final MinioClient minioClient;

    @Qualifier("minioBucket") // работает и без него
    private final String bucketName;

    // Вместо @Slf4j, т.к. lombok.* надежно отвалился!
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MinioService.class);


    public MinioService(MinioClient minioClient, String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }


    public void ensureBucketExists() {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Bucket created: {}", bucketName);
            }
        } catch (Exception e) {
            log.error("Error creating bucket: {}", bucketName, e);
            throw new RuntimeException(e);
        }
    }

    public String uploadFile(MultipartFile file) {
        ensureBucketExists();
        String objectName = file.getOriginalFilename();

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
        } catch (Exception e) {
            log.error("Error uploading file: {}", objectName, e);
            throw new RuntimeException(e);
        }

        return objectName;
    }

    public InputStream downloadFile(String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            log.error("Error downloading file: {}", objectName, e);
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            log.error("Error deleting file: {}", objectName, e);
            throw new RuntimeException(e);
        }
    }

    public List<Bucket> listBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            log.error("Error listing buckets", e);
            throw new RuntimeException(e);
        }
    }


    // возвращает список всех файлов (объектов) в бакете
    public List<String> listFiles() {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).build()
            );

            return StreamSupport.stream(results.spliterator(), false)
                    .map(itemResult -> {
                        try {
                            return itemResult.get().objectName();
                        } catch (Exception e) {
                            throw new RuntimeException("Error reading object name", e);
                        }
                    })
                    .toList();

        } catch (Exception e) {
            log.error("Error listing files in bucket: {}", bucketName, e);
            throw new RuntimeException(e);
        }
    }
}
