package ru.itis.soup2.mappers;

import ru.itis.soup2.dto.AttachmentDto;
import ru.itis.soup2.models.project.Attachment;
import java.util.List;
import java.util.stream.Collectors;

public final class AttachmentMapper {

    private AttachmentMapper() {}

    public static AttachmentDto toDto(Attachment attachment) {
        if (attachment == null) return null;

        return new AttachmentDto(
                attachment.getId(),
                attachment.getFileName(),
                attachment.getFileUrl(),
                attachment.getFileType(),
                attachment.getTask() != null ? attachment.getTask().getId() : null
        );
    }

    public static List<AttachmentDto> toDtoList(List<Attachment> attachments) {
        return attachments.stream()
                .map(AttachmentMapper::toDto)
                .collect(Collectors.toList());
    }
}