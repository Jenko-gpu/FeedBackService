package jenko.feedback.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FileMetadataDto {
    private UUID id;
    private String originalFilename;
    private Long fileSize;
    private String contentType;
    private String fileType;
    private String downloadUrl;
    private LocalDateTime uploadedAt;
}