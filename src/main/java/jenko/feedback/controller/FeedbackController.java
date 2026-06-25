package jenko.feedback.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jenko.feedback.dto.FeedbackCreateDto;
import jenko.feedback.dto.FeedbackResponseDto;
import jenko.feedback.service.FeedbackService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


/**
 * REST-контроллер для управления отзывами.
 * Все эндпоинты требуют аутентификации; userId и moderatorId извлекаются из контекста безопасности.
 */
@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новый отзыв", description = "Доступно авторизованным пользователям. Отзыв создаётся со статусом PENDING.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Отзыв успешно создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных")
    })
    public FeedbackResponseDto create(
            @Valid @RequestBody FeedbackCreateDto dto,
            @RequestAttribute("userId") UUID userId) {
        return feedbackService.createFeedback(userId, dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить отзыв по ID", description = "Доступно всем авторизованным пользователям.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзыв найден"),
            @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    })
    public FeedbackResponseDto get(@PathVariable UUID id) {
        return feedbackService.getFeedback(id);
    }


    @GetMapping
    @Operation(summary = "Получить список отзывов с фильтрацией и пагинацией",
            description = "Доступно всем авторизованным пользователям.")
    public Page<FeedbackResponseDto> list(
            @RequestParam(required = false) UUID targetId,
            @RequestParam(required = false) Integer ratingMin,
            @RequestParam(required = false) Integer ratingMax,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20) Pageable pageable) {
        return feedbackService.getFeedbacks(targetId, ratingMin, ratingMax, status, pageable);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить существующий отзыв",
            description = "Только автор отзыва может обновлять. Доступно только для отзывов в статусе PENDING.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзыв обновлён"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или отзыв не на модерации"),
            @ApiResponse(responseCode = "403", description = "Пользователь не является автором"),
            @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    })
    public FeedbackResponseDto update(
            @PathVariable UUID id,
            @Valid @RequestBody FeedbackCreateDto dto,
            @RequestAttribute("userId") UUID userId) {
        return feedbackService.updateFeedback(id, userId, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить отзыв по ID",
            description = "Только автор отзыва может удалить.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Отзыв удалён"),
            @ApiResponse(responseCode = "403", description = "Пользователь не является автором"),
            @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    })
    public void delete(
            @PathVariable UUID id,
            @RequestAttribute("userId") UUID userId) {
        feedbackService.deleteFeedback(id, userId);
    }



}
