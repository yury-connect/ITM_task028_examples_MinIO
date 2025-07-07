package com.example.miniodemo.controller;

import com.example.miniodemo.service.MinioService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/files")
//@RequiredArgsConstructor
public class FileController {

    private final MinioService minioService;


    public FileController(MinioService minioService) {
        this.minioService = minioService;
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFiles(@RequestParam("file") List<MultipartFile> fileList) {
        if (fileList == null || fileList.isEmpty()) {
            return ResponseEntity.badRequest().body("No files uploaded");
        }

        StringBuilder results = new StringBuilder();
        for (MultipartFile file : fileList) {
            String result = minioService.uploadFile(file);
            results.append("File uploaded: ").append(result).append("\n");
        }

        return ResponseEntity.ok(results.toString());
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
    public ResponseEntity<List<Map<String, String>>> listBuckets() {
        var buckets = minioService.listBuckets().stream()
                .map(b -> Map.of(
                        "name", b.name(),
                        "creationDate", b.creationDate().toString()
                ))
                .toList();
        return ResponseEntity.ok(buckets);
    }

    @GetMapping
    public ResponseEntity<List<String>> listFiles() {
        List<String> files = minioService.listFiles();
        return ResponseEntity.ok(files);
    }
}