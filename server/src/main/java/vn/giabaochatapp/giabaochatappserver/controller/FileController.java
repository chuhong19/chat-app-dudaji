package vn.giabaochatapp.giabaochatappserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.giabaochatapp.giabaochatappserver.data.domains.File;
import vn.giabaochatapp.giabaochatappserver.data.repository.FileRepository;
import vn.giabaochatapp.giabaochatappserver.services.AWSS3Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final AWSS3Service s3Service;

    private final FileRepository fileRepository;

    public FileController(AWSS3Service s3Service, FileRepository fileRepository) {
        this.s3Service = s3Service;
        this.fileRepository = fileRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = s3Service.uploadFile(file);

            File fileEntity = new File();
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setUrl(fileUrl);
            fileEntity.setUploadedAt(LocalDateTime.now());
            fileRepository.save(fileEntity);

            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFileById(@PathVariable Long id) {
        Optional<File> fileEntity = fileRepository.findById(id);
        if (fileEntity.isPresent()) {
            return ResponseEntity.ok(fileEntity.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
    }

    @PostMapping("/upload-to-room/{roomId}")
    public ResponseEntity<?> uploadFileToRoom(@PathVariable Long roomId, @RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = s3Service.uploadFile(file);

            File fileEntity = new File();
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setUrl(fileUrl);
            fileEntity.setUploadedAt(LocalDateTime.now());
            fileRepository.save(fileEntity);

            return ResponseEntity.ok(fileEntity);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught IllegalArgumentException: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<String> getPresignedUrl(@PathVariable String fileName) {
        String presignedUrl = s3Service.generatePresignedUrl(fileName);
        return ResponseEntity.ok(presignedUrl);
    }
}
