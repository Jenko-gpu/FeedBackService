package jenko.feedback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jenko.feedback.dto.FeedbackResponseDto;
import jenko.feedback.service.ModerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Контроллер для административных операций модерации отзывов.
 * Доступен только пользователям с ролью MODERATOR или ADMIN.
 */
@RestController
@RequestMapping("/api/v1/admin/moderation")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MODERATOR')")
public class AdminModerationController {

    private final ModerationService moderationService;

    // ==================== GET PENDING ====================
    @GetMapping("/pending")
    @Operation(summary = "Получить список отзывов, ожидающих модерации",
            description = "Доступно только модераторам и администраторам.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список получен"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    public Page<FeedbackResponseDto> getPendingFeedbacks(
            @PageableDefault(size = 20) Pageable pageable) {
        return moderationService.getPendingFeedbacks(pageable);
    }

    // ==================== APPROVE ====================
    @PutMapping("/{id}/approve")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Одобрить отзыв", description = "Переводит отзыв в статус APPROVED.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзыв одобрен"),
            @ApiResponse(responseCode = "400", description = "Отзыв не в статусе PENDING или ошибка валидации"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    })
    public FeedbackResponseDto approve(
            @PathVariable UUID id,
            @RequestParam(required = false) String comment,
            @RequestAttribute("moderatorId") UUID moderatorId) {
        return moderationService.approveFeedback(id, moderatorId, comment);
    }

    // ==================== REJECT ====================
    @PutMapping("/{id}/reject")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Отклонить отзыв", description = "Переводит отзыв в статус REJECTED.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзыв отклонён"),
            @ApiResponse(responseCode = "400", description = "Отзыв не в статусе PENDING или ошибка валидации"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    })
    public FeedbackResponseDto reject(
            @PathVariable UUID id,
            @RequestParam(required = false) String comment,
            @RequestAttribute("moderatorId") UUID moderatorId) {
        return moderationService.rejectFeedback(id, moderatorId, comment);
    }
}