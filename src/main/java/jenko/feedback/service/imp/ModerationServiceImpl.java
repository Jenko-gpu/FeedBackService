package jenko.feedback.service.imp;

import jenko.feedback.dto.FeedbackResponseDto;
import jenko.feedback.service.FeedbackService;
import jenko.feedback.service.ModerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModerationServiceImpl implements ModerationService {

    private final FeedbackService feedbackService;

    @Override
    public Page<FeedbackResponseDto> getPendingFeedbacks(Pageable pageable) {
        return feedbackService.getPendingFeedbacks(pageable);
    }

    @Override
    @Transactional
    public FeedbackResponseDto approveFeedback(UUID feedbackId, UUID moderatorId, String comment) {
        log.info("Approving feedback {} by moderator {}", feedbackId, moderatorId);
        return feedbackService.moderateFeedback(feedbackId, moderatorId, "approve", comment);
    }

    @Override
    @Transactional
    public FeedbackResponseDto rejectFeedback(UUID feedbackId, UUID moderatorId, String comment) {
        log.info("Rejecting feedback {} by moderator {}", feedbackId, moderatorId);
        return feedbackService.moderateFeedback(feedbackId, moderatorId, "reject", comment);
    }
}