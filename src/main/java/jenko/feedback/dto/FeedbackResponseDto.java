package jenko.feedback.dto;

import jenko.feedback.core.FeedbackStatus;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class FeedbackResponseDto {
    private UUID id;
    private UUID userId;
    private UUID targetId;
    private Integer rating;
    private String title;
    private String content;
    private FeedbackStatus status;
    private UUID moderatorId;
    private String moderationComment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FileAttachmentDto> attachments; // метаданные файлов
}