package jenko.feedback.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FileAttachmentDto {
    private UUID id;
    private UUID fileMetadataId;
    private String fileName;
    private Long fileSize;
    private String fileType;
    private String downloadUrl;
    private LocalDateTime uploadedAt;
}