package ru.itis.soup2.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.soup2.models.project.Attachment;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDto {

    private Integer id;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private Integer taskId;

    public static AttachmentDto from(Attachment attachment) {
        if (attachment == null) return null;

        return AttachmentDto.builder()
                .id(attachment.getId())
                .fileName(attachment.getFileName())
                .fileUrl(attachment.getFileUrl())
                .fileType(attachment.getFileType())
                .taskId(attachment.getTask() != null ? attachment.getTask().getId() : null)
                .build();
    }
}