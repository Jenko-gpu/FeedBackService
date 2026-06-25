package jenko.feedback.model;



import jakarta.persistence.*;

import jenko.feedback.core.EntityType;
import jenko.feedback.core.FeedbackStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "feedbacks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId; // автор отзыва

    @Column(name = "target_id", nullable = false)
    private UUID targetId; // идентификатор объекта отзыва

    @Column(nullable = false)
    private Integer rating; // 1–5

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FeedbackStatus status;

    @Column(name = "moderator_id")
    private UUID moderatorId; // кто модерировал

    @Column(name = "moderation_comment", columnDefinition = "TEXT")
    private String moderationComment;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Связь с прикреплёнными файлами
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "entity_type = 'FEEDBACK'")
    private List<FileAttachment> attachments = new ArrayList<>();

    public void addAttachment(FileAttachment attachment) {
        attachments.add(attachment);
        attachment.setEntityType(EntityType.FEEDBACK);
        attachment.setEntityId(this.id);
    }

    public void removeAttachment(FileAttachment attachment) {
        attachments.remove(attachment);
    }
}