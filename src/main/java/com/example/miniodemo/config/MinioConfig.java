package com.example.miniodemo.config;

import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
//@RequiredArgsConstructor
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    @Value("${minio.url}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.secure}")
    private boolean secure;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Bean
    @ConfigurationProperties(prefix = "minio.bucket")
    public String minioBucket() {
        return bucket;
    }



    // Логируем прочитанное из свойств
    @PostConstruct
    public void init() {
        System.out.println("➡ MINIO CONFIG:");
        System.out.println("URL: " + endpoint);
        System.out.println("KEY: " + accessKey);
        System.out.println("SECRET: " + secretKey);
    }
}