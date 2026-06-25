package jenko.feedback.model;

import jakarta.persistence.*;

import jenko.feedback.core.EntityType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "file_attachments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "file_metadata_id", nullable = false)
    private UUID fileMetadataId; // ссылка на файл в сервисе для работы с файлами

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false, length = 20)
    private EntityType entityType;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId; // ID родительской сущности

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}