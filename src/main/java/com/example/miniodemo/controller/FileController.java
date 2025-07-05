package com.example.miniodemo.controller;

import com.example.miniodemo.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final MinioService minioService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String objectName = minioService.uploadFile(file);
        return ResponseEntity.ok("File uploaded: " + objectName);
    }

    @GetMapping("/{objectName}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String objectName) {
        var stream = minioService.downloadFile(objectName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(stream));
    }

    @DeleteMapping("/{objectName}")
    public ResponseEntity<String> deleteFile(@PathVariable String objectName) {
        minioService.deleteFile(objectName);
        return ResponseEntity.ok("File deleted: " + objectName);
    }

    @GetMapping("/buckets")
    public ResponseEntity<List<String>> listBuckets() {
        var buckets = minioService.listBuckets().stream()
                .map(b -> b.name())
                .toList();
        return ResponseEntity.ok(buckets);
    }
}