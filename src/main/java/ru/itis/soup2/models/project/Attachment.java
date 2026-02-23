package ru.itis.soup2.models.project;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "attachments", schema = "project")
public class Attachment {
    @Id
    @Column(name = "attachment_id")
    private Integer attachmentId;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "filename")
    private String fileName;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "filetype")
    private String fileType;
}
