package jenko.feedback.service.imp;

import jakarta.persistence.EntityNotFoundException;
import jenko.feedback.client.FileStorageClient;
import jenko.feedback.core.EntityType;
import jenko.feedback.core.FeedbackStatus;
import jenko.feedback.core.ValidationException;
import jenko.feedback.dto.FeedbackCreateDto;
import jenko.feedback.dto.FeedbackResponseDto;
import jenko.feedback.event.FeedbackCreatedEvent;
import jenko.feedback.event.FeedbackModeratedEvent;
import jenko.feedback.mapper.FeedbackMapper;
import jenko.feedback.model.Feedback;
import jenko.feedback.model.FileAttachment;
import jenko.feedback.repository.FeedbackRepository;
import jenko.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final FileStorageClient fileStorageClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional
    public FeedbackResponseDto createFeedback(UUID userId, FeedbackCreateDto dto) {
        Feedback feedback = feedbackMapper.toEntity(dto);
        feedback.setUserId(userId);
        feedback.setStatus(FeedbackStatus.PENDING);

        // Привязываем загруженные файлы
        if (dto.getAttachmentFileIds() != null && !dto.getAttachmentFileIds().isEmpty()) {
            List<FileAttachment> attachments = dto.getAttachmentFileIds().stream()
                    .map(fileId -> FileAttachment.builder()
                            .fileMetadataId(fileId)
                            .entityType(EntityType.FEEDBACK)
                            .build())
                    .toList();
            attachments.forEach(feedback::addAttachment);
        }

        Feedback saved = feedbackRepository.save(feedback);
        log.info("Created feedback with id: {}", saved.getId());


        FeedbackCreatedEvent event = new FeedbackCreatedEvent(
                saved.getId(),
                saved.getUserId(),
                saved.getTargetId(),
                saved.getRating(),
                saved.getTitle(),
                saved.getCreatedAt()
        );
        kafkaTemplate.send("feedback-events", event);
        log.debug("Sent FeedbackCreatedEvent for feedback {}", saved.getId());


        return enrichWithFileDetails(feedbackMapper.toResponseDto(saved));
    }

    @Override
    public FeedbackResponseDto getFeedback(UUID feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found with id: " + feedbackId));
        return enrichWithFileDetails(feedbackMapper.toResponseDto(feedback));
    }

    @Override
    public Page<FeedbackResponseDto> getFeedbacks(UUID targetId, Integer ratingMin, Integer ratingMax,
                                                  String statusStr, Pageable pageable) {
        FeedbackStatus status = statusStr != null ? FeedbackStatus.valueOf(statusStr.toUpperCase()) : null;
        Page<Feedback> page = feedbackRepository.search(
                targetId, status, ratingMin, ratingMax, null, null, pageable);
        return page.map(feedbackMapper::toResponseDto);
    }

    @Override
    @Transactional
    public FeedbackResponseDto updateFeedback(UUID feedbackId, UUID userId, FeedbackCreateDto dto) {
        Feedback feedback = findFeedbackOrThrow(feedbackId);

        // Только автор может редактировать
        if (!feedback.getUserId().equals(userId)) {
            throw new ValidationException("User is not the author of this feedback");
        }

        // Разрешаем редактирование только если отзыв ещё на модерации
        if (feedback.getStatus() != FeedbackStatus.PENDING) {
            throw new ValidationException("Cannot update feedback that is already moderated");
        }

        // Обновляем основные поля
        feedback.setRating(dto.getRating());
        feedback.setTitle(dto.getTitle());
        feedback.setContent(dto.getContent());

        // Обновляем вложения: удаляем старые и добавляем новые
        updateAttachments(feedback, dto.getAttachmentFileIds());

        Feedback updated = feedbackRepository.save(feedback);
        log.info("Feedback updated: id={}, userId={}", updated.getId(), userId);

        return enrichWithFileDetails(feedbackMapper.toResponseDto(updated));
    }

    @Override
    @Transactional
    public void deleteFeedback(UUID feedbackId, UUID userId) {
        Feedback feedback = findFeedbackOrThrow(feedbackId);

        // Только автор может удалять
        // TODO добавить возможность удалять администратору
        if (!feedback.getUserId().equals(userId)) {
            throw new ValidationException("User is not the author of this feedback");
        }

        feedbackRepository.delete(feedback);
        log.info("Feedback deleted: id={}, userId={}", feedbackId, userId);
    }

    @Override
    @Transactional
    public FeedbackResponseDto moderateFeedback(UUID feedbackId, UUID moderatorId, String decision, String comment) {
        Feedback feedback = findFeedbackOrThrow(feedbackId);

        // Можно модерировать только отзывы в статусе PENDING
        if (feedback.getStatus() != FeedbackStatus.PENDING) {
            throw new ValidationException("Feedback is not pending moderation");
        }

        FeedbackStatus newStatus;
        if ("approve".equalsIgnoreCase(decision)) {
            newStatus = FeedbackStatus.APPROVED;
        } else if ("reject".equalsIgnoreCase(decision)) {
            newStatus = FeedbackStatus.REJECTED;
        } else {
            throw new ValidationException("Invalid moderation decision. Use 'approve' or 'reject'");
        }

        feedback.setStatus(newStatus);
        feedback.setModeratorId(moderatorId);
        feedback.setModerationComment(comment);

        Feedback moderated = feedbackRepository.save(feedback);
        log.info("Feedback moderated: id={}, decision={}, moderatorId={}", feedbackId, decision, moderatorId);

        FeedbackModeratedEvent event = new FeedbackModeratedEvent(
                moderated.getId(),
                moderated.getModeratorId(),
                moderated.getStatus(),
                moderated.getModerationComment(),
                LocalDateTime.now()
        );
        kafkaTemplate.send("feedback-moderated", event);
        log.debug("Sent FeedbackModeratedEvent for feedback {}", moderated.getId());

        return enrichWithFileDetails(feedbackMapper.toResponseDto(moderated));
    }

    @Override
    public Page<FeedbackResponseDto> getPendingFeedbacks(Pageable pageable) {
        Page<Feedback> pendingPage = feedbackRepository.findByStatus(FeedbackStatus.PENDING, pageable);
        return pendingPage.map(feedbackMapper::toResponseDto);
    }

    // ---------- Вспомогательные методы ----------

    private Feedback findFeedbackOrThrow(UUID feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found with id: " + feedbackId));
    }

    /**
     * Обновляет список прикреплённых файлов для отзыва.
     * Удаляет старые связи и создаёт новые на основе переданных ID файлов.
     */
    private void updateAttachments(Feedback feedback, List<UUID> newFileIds) {
        // Очищаем старые связи
        feedback.getAttachments().clear();

        if (newFileIds != null && !newFileIds.isEmpty()) {
            List<FileAttachment> newAttachments = newFileIds.stream()
                    .map(fileId -> FileAttachment.builder()
                            .fileMetadataId(fileId)
                            .entityType(EntityType.FEEDBACK)
                            .entityId(feedback.getId())
                            .build())
                    .toList();
            feedback.getAttachments().addAll(newAttachments);
        }
        // Если newFileIds == null или пуст, то все вложения удаляются
    }

    /**
     * Обогащает DTO данными о файлах
     */
    private FeedbackResponseDto enrichWithFileDetails(FeedbackResponseDto dto) {
        if (dto.getAttachments() != null) {
            dto.getAttachments().forEach(attachmentDto -> {
                // TODO заполнить fileName, fileSize, fileType, downloadUrl из сервиса хранения файлов
            });
        }
        return dto;
    }
}