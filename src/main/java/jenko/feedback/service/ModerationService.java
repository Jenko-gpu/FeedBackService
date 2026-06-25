package jenko.feedback.service;

import jenko.feedback.dto.FeedbackResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Сервис для административных операций модерации.
 */
public interface ModerationService {

    /**
     * Получить все отзывы, ожидающие модерации.
     */
    Page<FeedbackResponseDto> getPendingFeedbacks(Pageable pageable);

    /**
     * Одобрить отзыв.
     */
    FeedbackResponseDto approveFeedback(UUID feedbackId, UUID moderatorId, String comment);

    /**
     * Отклонить отзыв.
     */
    FeedbackResponseDto rejectFeedback(UUID feedbackId, UUID moderatorId, String comment);
}