package jenko.feedback.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackCreatedEvent {
    private UUID feedbackId;
    private UUID userId;
    private UUID targetId;
    private Integer rating;
    private String title;
    private LocalDateTime createdAt;
}