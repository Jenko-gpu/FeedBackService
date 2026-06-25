package jenko.feedback.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class FeedbackCreateDto {

    @NotNull
    private UUID targetId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    @NotBlank
    private String title;

    private String content;

    // Список идентификаторов файлов, уже загруженных в сервис хранения файлов
    private List<UUID> attachmentFileIds;
}
