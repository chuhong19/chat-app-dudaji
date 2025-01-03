package vn.giabaochatapp.giabaochatappserver.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class AWSS3Service {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    private S3Client s3Client;

    private final S3Presigner s3Presigner;

    public AWSS3Service(S3Presigner s3Presigner) {
        this.s3Presigner = s3Presigner;
    }

    @PostConstruct
    public void init() {
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .build();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        final long MAX_SIZE = 25 * 1024 * 1024;

        final List<String> ALLOWED_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg", "pdf", "txt", "docx", "xlsx");

        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("File size exceeds the maximum limit of 25MB");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !hasAllowedExtension(originalFilename, ALLOWED_EXTENSIONS)) {
            throw new IllegalArgumentException("Invalid file extension. Allowed extensions are: " + ALLOWED_EXTENSIONS);
        }

        String fileName = UUID.randomUUID() + "_" + originalFilename;

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .contentType(file.getContentType())
                        .contentDisposition("attachment; filename=\"" + originalFilename + "\"")
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
    }

    private boolean hasAllowedExtension(String filename, List<String> allowedExtensions) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        return allowedExtensions.contains(extension);
    }

    public String generatePresignedUrl(String fileName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofMinutes(1))
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}
