package jenko.feedback.mapper;


import jenko.feedback.dto.FeedbackCreateDto;
import jenko.feedback.dto.FeedbackResponseDto;
import jenko.feedback.model.Feedback;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedbackMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "moderatorId", ignore = true)
    @Mapping(target = "moderationComment", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    Feedback toEntity(FeedbackCreateDto dto);


    @Mapping(target = "attachments", source = "attachments", qualifiedByName = "toAttachmentDto")
    FeedbackResponseDto toResponseDto(Feedback feedback);

}