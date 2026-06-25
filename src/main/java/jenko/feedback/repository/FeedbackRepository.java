package jenko.feedback.repository;


import jenko.feedback.core.FeedbackStatus;
import jenko.feedback.model.Feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {

    Page<Feedback> findByTargetId(UUID targetId, Pageable pageable);

    Page<Feedback> findByStatus(FeedbackStatus status, Pageable pageable);

    Page<Feedback> findByUserId(UUID userId, Pageable pageable);

    @Query("SELECT f FROM Feedback f WHERE " +
            "(:targetId IS NULL OR f.targetId = :targetId) AND " +
            "(:status IS NULL OR f.status = :status) AND " +
            "(:ratingFrom IS NULL OR f.rating >= :ratingFrom) AND " +
            "(:ratingTo IS NULL OR f.rating <= :ratingTo) AND " +
            "(:createdFrom IS NULL OR f.createdAt >= :createdFrom) AND " +
            "(:createdTo IS NULL OR f.createdAt <= :createdTo)")
    Page<Feedback> search(@Param("targetId") UUID targetId,
                          @Param("status") FeedbackStatus status,
                          @Param("ratingFrom") Integer ratingFrom,
                          @Param("ratingTo") Integer ratingTo,
                          @Param("createdFrom") LocalDateTime createdFrom,
                          @Param("createdTo") LocalDateTime createdTo,
                          Pageable pageable);
}