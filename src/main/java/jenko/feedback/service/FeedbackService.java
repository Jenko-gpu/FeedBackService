package jenko.feedback.service;

import jenko.feedback.dto.FeedbackCreateDto;
import jenko.feedback.dto.FeedbackResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FeedbackService {
    FeedbackResponseDto createFeedback(UUID userId, FeedbackCreateDto dto);
    FeedbackResponseDto getFeedback(UUID feedbackId);
    Page<FeedbackResponseDto> getFeedbacks(UUID targetId, Integer ratingMin, Integer ratingMax,
                                           String status, Pageable pageable);
    FeedbackResponseDto updateFeedback(UUID feedbackId, UUID userId, FeedbackCreateDto dto);
    void deleteFeedback(UUID feedbackId, UUID userId);
    /**
     * Получить страницу отзывов со статусом PENDING (ожидают модерации).
     */
    Page<FeedbackResponseDto> getPendingFeedbacks(Pageable pageable);


    /**
     * Выполнить модерацию отзыва (одобрить или отклонить).
     * @param feedbackId ID отзыва
     * @param moderatorId ID модератора
     * @param decision "approve" или "reject"
     * @param comment комментарий модератора (опционально)
     * @return обновлённый DTO
     */
    FeedbackResponseDto moderateFeedback(UUID feedbackId, UUID moderatorId, String decision, String comment);
}