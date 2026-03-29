package ru.itis.soup2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}