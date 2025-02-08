package com.filezip.processor.application.core.repository.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("video-document")
public class VideoDocument {

    @Id
    private String id;
    private String name;
    private String userId;
    private String userName;
    private String userEmail;
    private String directory;
    private String correlationId;
    private Double intervalFrame;
    private String processorStatus;
}
