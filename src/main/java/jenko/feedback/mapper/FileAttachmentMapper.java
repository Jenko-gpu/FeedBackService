package jenko.feedback.mapper;

import jenko.feedback.dto.FileAttachmentDto;
import jenko.feedback.model.FileAttachment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring")
public abstract class FileAttachmentMapper {


    @Mapping(target = "fileName", ignore = true) // заполняется через сервис хранения файлов
    @Mapping(target = "fileSize", ignore = true)
    @Mapping(target = "fileType", ignore = true)
    @Mapping(target = "downloadUrl", ignore = true)
    @Mapping(target = "uploadedAt", ignore = true)
    public abstract FileAttachmentDto toDto(FileAttachment attachment);

    @Named("toAttachmentDto")
    public FileAttachmentDto toAttachmentDto(FileAttachment attachment) {
        return toDto(attachment);
    }
}
