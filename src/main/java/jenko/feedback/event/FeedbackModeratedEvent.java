package jenko.feedback.event;

import jenko.feedback.core.FeedbackStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackModeratedEvent {
    private UUID feedbackId;
    private UUID moderatorId;
    private FeedbackStatus newStatus;
    private String moderationComment;
    private LocalDateTime moderatedAt;
}